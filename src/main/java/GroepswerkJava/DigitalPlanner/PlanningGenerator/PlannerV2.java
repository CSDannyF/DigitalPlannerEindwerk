package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.model.LogType;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.HOURS;

@Service("plannerV2")
public class PlannerV2 implements PlanningGenerator {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private LogDetailService logDetailService;

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

    @Autowired
    private PlanningStatusService planningStatusService;


    @Override
    public Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays) {
        Response response = new Response();
        List<LogDetailDto> logging = new ArrayList<>();

        //    //for keeping track what day it is, used to determine shift time
        int dayCounter = -1;

        if (workingDays.size() != 7) {
            logDetailService.addLog(logging, "Week of WorkingDays not completely configured.", LogType.INFO);
            return response;
        } else {
            logDetailService.addLog(logging, "Week of WorkingDays is complete.", LogType.INFO);

            List<PlanningDto> planningList;
            List<DaysOffWeek> daysOffWeekList = sortWorkingDaysForComplexity(workingDays);

            checkIfWeekAlreadyPlanned(workingDays, staff, logging);


            planningList = populateShifts(workingDays, daysOffWeekList, staff, dayCounter, logging);
            response.setPlanningList(planningList);
        }
        staffService.saveAllStaff(staff);

        return response;
    }


    private List<PlanningDto> populateShifts(List<WorkingDayDto> workingDays, List<DaysOffWeek> daysOffWeekList,
                                             List<StaffDto> staff, int dayCounter, List<LogDetailDto> logging) {

        logDetailService.addLog(logging, "Start creating shifts.", LogType.INFO);

        int totalShiftTime = 8;
        List<PlanningDto> planningList = new ArrayList<>();

        for (DaysOffWeek dayOffWeek : daysOffWeekList) {
            dayCounter++;

            //sorted on most complex days first, so easiest days are planned last
            WorkingDayDto day = getWorkingDayByDayOfWeek(workingDays, dayOffWeek);

            String planningStatus = "Planned OK";
            List<PlanningDetailDto> shifts = new ArrayList<>();

            String currentStatus = day.getPlanningDto().getPlanningStatusDto().getPlanningStatusName();

            if (day.isOpen()) {
                if (currentStatus.equalsIgnoreCase("Not planned")
                        || currentStatus.equalsIgnoreCase("Planned NOK")) {
                    List<StaffDto> availableStaff = getAvailableStaff(day, new ArrayList<>(staff));
                    logDetailService.addLog(logging, "Start planning " + day.getDate().getDayOfWeek() + ".", LogType.INFO);


                    //Get amount of shifts that needs to be planned
                    int requiredShifts = day.getMinimalOccupation();

                    int openHours = (int) HOURS.between(day.getStartTime(), day.getEndTime());

                    // if planned (manually) but not ok, get amount of shifts that still needs to be planned
                    if (day.getPlanningDto().getPlanningStatusDto().getPlanningStatusName().equalsIgnoreCase("Planned NOK")) {
                        requiredShifts -= day.getPlanningDto().getPlanningDetails().size();
                        logDetailService.addLog(logging, requiredShifts + " Shifts are needed for this day.", LogType.INFO);

                    }

                    for (int shiftCounter = 0; shiftCounter < requiredShifts; shiftCounter++) {
                        logDetailService.addLog(logging, "Start planning shift " + (shiftCounter + 1), LogType.INFO);

                        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
                        StaffDto assignedStaff = null;

                        //if not empty
                        if (!availableStaff.isEmpty()) {
                            assignedStaff = availableStaff.getFirst();
                            planningStatus = "Planned OK";

                            logDetailService.addLog(logging, "Shift " + (shiftCounter + 1) + " is assigned to "
                                    + assignedStaff.getFirstName() + ", " + assignedStaff.getLastName(), LogType.INFO);

                            totalShiftTime = getShiftTime(assignedStaff, openHours, dayCounter, logging);
                            assignedStaff.setHoursInDebt(assignedStaff.getHoursInDebt() - totalShiftTime);


                        } else {
                            planningStatus = "Planned NOK";
                            logDetailService.addLog(logging, "No more available staff. Create Open Shift instead", LogType.INFO);
                        }

                        planningDetailDto.setStaff(assignedStaff);
                        setStartAndEndTimeShift(planningDetailDto, assignedStaff, day, shiftCounter, openHours, dayCounter, logging);

                        //if a staff is assigned, the shift is planned, if staff is null, the shift is an open shift
                        if (planningDetailDto.getStaff() != null) {
                            planningDetailDto.setAvailability(availabilityService.getByAvailabilityType("Planned"));
                        } else {
                            planningDetailDto.setAvailability(availabilityService.getByAvailabilityType("OPEN SHIFT"));
                        }
                        availableStaff.remove(assignedStaff);

                        planningDetailDto.setWorkMinutesPerDay(totalShiftTime * 60);

                        shifts.add(planningDetailDto);

                        logDetailService.addLog(logging, "Shift " + (shiftCounter + 1) + " finalized: " + planningDetailDto, LogType.INFO);
                    }
                }
            } else {
                planningStatus = "Closed";

                logDetailService.addLog(logging, "Skipping closed day.", LogType.INFO);
            }

            //Sort shifts based on startTime
            shifts.sort(Comparator.comparing(PlanningDetailDto::getStartTime));
            day.getPlanningDto().setPlanningDetails(shifts);
            day.getPlanningDto().setWorkingDayDto(day);
            day.getPlanningDto().setPlanningStatusDto(planningStatusService.findByPlanningStatusName(planningStatus));
            planningList.add(day.getPlanningDto());
        }

        planningList.sort(Comparator.comparingLong(PlanningDto::getPlanningId));

        return planningList;
    }


    private void setStartAndEndTimeShift(PlanningDetailDto planningDetailDto, StaffDto planStaff, WorkingDayDto day,
                                         int shiftCounter, int dayHours, int dayCounter, List<LogDetailDto> logging) {
        int shiftTime = getShiftTime(planStaff, dayHours, dayCounter, logging);
        logDetailService.addLog(logging, "Calculating start and end time of shift " + (shiftCounter + 1), LogType.INFO);


        // if total openHours this day is more as 16h, a midday shift is created, else there is just opening and closing shifts
        if (dayHours > 16) {
            if ((shiftCounter + 1) % 3 == 0) {
                // Midday shift
                LocalTime startMiddayShift = day.getStartTime().plusHours(dayHours - 16);
                planningDetailDto.setStartTime(startMiddayShift);
                planningDetailDto.setEndTime(startMiddayShift.plusHours(shiftTime));
            } else {
                setOpeningAndClosingShift(shiftCounter, planningDetailDto, day, shiftTime);
            }
        } else {
            setOpeningAndClosingShift(shiftCounter, planningDetailDto, day, shiftTime);
        }
    }

    private void setOpeningAndClosingShift(int i, PlanningDetailDto planningDetailDto, WorkingDayDto day, int shiftTime) {
        if ((i) % 2 == 0) {
            // Opening shift
            planningDetailDto.setStartTime(day.getStartTime());
            planningDetailDto.setEndTime(day.getStartTime().plusHours(shiftTime));
        } else {
            // Closing shift
            planningDetailDto.setStartTime(day.getEndTime().minusHours(shiftTime));
            planningDetailDto.setEndTime(day.getEndTime());
        }
    }

    // calculates length of a shift, if too many hoursInDebt, set shift to 9 instead of 8 hours
    private int getShiftTime(StaffDto planStaff, int dayHours, int dayCounter, List<LogDetailDto> logging) {
        logDetailService.addLog(logging, "Calculating shift length", LogType.INFO);

        int hoursPerShift = 8;

        if (planStaff != null) {
            //if too many hours in debt, make shifts longer
            if (planStaff.getHoursInDebt() > planStaff.getContractType().getHoursPerWeek() - hoursPerShift * dayCounter) {
                hoursPerShift += 1;
            }
        }

        //if the openingsHours of a day is less then 8 hours total
        if (dayHours <= 8) {
            hoursPerShift = dayHours;
        }
        logDetailService.addLog(logging, "shift time: " + hoursPerShift + "h", LogType.INFO);

        return hoursPerShift;

    }


    //Sort list of workingDays so that most complex days (with most unavailable staff) are planned first
    private List<DaysOffWeek> sortWorkingDaysForComplexity(List<WorkingDayDto> workingDays) {
        List<DaysOffWeek> daysOffWeekList = new ArrayList<>();

        Map<DaysOffWeek, WorkingDayDto> daysOffWeekToDtoMap = new HashMap<>();
        for (WorkingDayDto dayDto : workingDays) {
            DaysOffWeek daysOffWeek = daysOfWeekRepository.findByDayName(dayDto.getDate().getDayOfWeek().toString());
            daysOffWeekList.add(daysOffWeek);
            daysOffWeekToDtoMap.put(daysOffWeek, dayDto);
        }

        // Sort daysOffWeekList based on combined complexity score s(staff absence + staff fixedDaysOff)
        daysOffWeekList.sort(Comparator.comparingInt(o -> {
            int staffCount = o.getStaff().size();

            int planningDetailsCount = unavailabilitiesPerPlanning(daysOffWeekToDtoMap.get(o));

            return staffCount + planningDetailsCount;
        }));

        Collections.reverse(daysOffWeekList);
        return daysOffWeekList;
    }

    //  count "Sick" or "Absence" planning details on a certain day
    private int unavailabilitiesPerPlanning(WorkingDayDto workingDayDto) {
        if (workingDayDto.getPlanningDto().getPlanningDetails() == null) {
            return 0;
        }
        return (int) workingDayDto.getPlanningDto().getPlanningDetails().stream()
                .filter(detail -> {
                    String availabilityType = detail.getAvailability().getAvailabilityType();
                    return "Sick".equals(availabilityType) || "Absence".equals(availabilityType);
                })
                .count();
    }


    //returns the correct workingDay for the corresponding dayOfWeek
    private WorkingDayDto getWorkingDayByDayOfWeek(List<WorkingDayDto> workingDays, DaysOffWeek dayOffWeek) {
        WorkingDayDto day = null;
        for (WorkingDayDto workingDayDto : workingDays) {
            if (workingDayDto.getDate().getDayOfWeek().toString().equalsIgnoreCase(dayOffWeek.getDayName())) {
                day = workingDayDto;
                break;
            }
        }
        return day;
    }


    //returns a list of available staff, sorted based on highest hoursInDebt first, then highest hoursPerWeek in contract
    private List<StaffDto> getAvailableStaff(WorkingDayDto day, List<StaffDto> staffList) {
        Set<StaffDto> notAvailableStaff = new HashSet<>();
        // make a copy (instead of reference) of list with all staff
        List<StaffDto> availableStaff = new ArrayList<>(staffList);

        for (StaffDto staffDto : staffList) {
            // check start and endDate of staff to see if available, and check hoursInDebt
            if (staffDto.getHoursInDebt() < 1
                    || staffDto.getStartDate().isAfter(day.getDate())
                    || (staffDto.getEndDate() != null && staffDto.getEndDate().isBefore(day.getDate()))) {
                notAvailableStaff.add(staffDto);
            }

            // check fixedDaysOff to see if staff is available on  this day
            if (!staffDto.getFixedDaysOff().isEmpty()) {
                for (DaysOffWeekDto daysOffWeekDto : staffDto.getFixedDaysOff()) {
                    if (daysOffWeekDto.getDayName().equalsIgnoreCase(day.getDate().getDayOfWeek().toString())) {
                        notAvailableStaff.add(staffDto);
                        break;
                    }
                }
            }

            // check if there is already a planningDetail with this staff on this day, and checks the availabilityType
            for (PlanningDetailDto planningDetailDto : day.getPlanningDto().getPlanningDetails()) {
                if (planningDetailDto.getStaff() != null) {
                    if (staffDto.getStaffId() == planningDetailDto.getStaff().getStaffId() &&
                            (planningDetailDto.getAvailability().getAvailabilityId() == 4 ||
                                    planningDetailDto.getAvailability().getAvailabilityId() == 3 ||
                                    planningDetailDto.getAvailability().getAvailabilityId() == 2)) {

                        notAvailableStaff.add(staffDto);
                        break;
                    }
                }
            }
        }
        //removes all unavailable staff
        availableStaff.removeAll(notAvailableStaff);

        //sorts the available staff so that staff with most (hoursInDebt / contractHours) comes first
        Comparator<StaffDto> comparator = Comparator.comparingDouble(staff -> (double) staff.getHoursInDebt() / staff.getContractType().getHoursPerWeek());
        availableStaff.sort(comparator);
        Collections.reverse(availableStaff);

        return availableStaff;
    }


    // not a great method, probably better way to determine if a week is planned.
    // if a week has more than 3 days that are not planned, it is seen as an unplanned week, and thus
    // hoursInDebt for the staff are set by their contract hoursPerWeek
    private void checkIfWeekAlreadyPlanned(List<WorkingDayDto> workingDays, List<StaffDto> staff, List<LogDetailDto> logging) {
        logDetailService.addLog(logging, "Check if week is already planned.", LogType.INFO);

        int counter = 0;
        for (WorkingDayDto workingDayDto : workingDays) {
            String status = workingDayDto.getPlanningDto().getPlanningStatusDto().getPlanningStatusName();
            if (status.equalsIgnoreCase("Not planned")) {
                counter++;
            }
        }
        if (counter > 3) {
            setStaffHoursInDebt(staff, workingDays);
            logDetailService.addLog(logging, "Week is not yet fully planned.", LogType.INFO);
        } else{
            logDetailService.addLog(logging, "Week is already planned.", LogType.INFO);

        }
    }


    //Added a field in StaffDto (hoursInDebt) to keep track of how many hours still needs to be planned in a week,
    //this is also for keeping track of plus minus hours
    private void setStaffHoursInDebt(List<StaffDto> staff, List<WorkingDayDto> workingDayDtos) {
        for (StaffDto staffDto : staff) {
            if (staffDto.getEndDate() == null) {
                staffDto.setHoursInDebt(staffDto.getHoursInDebt() + staffDto.getContractType().getHoursPerWeek());
            } else {
                //Check if staff is still active
                if (!staffDto.getEndDate().isBefore(workingDayDtos.getFirst().getDate())) {
                    staffDto.setHoursInDebt(staffDto.getHoursInDebt() + staffDto.getContractType().getHoursPerWeek());
                }
            }
        }
    }
}