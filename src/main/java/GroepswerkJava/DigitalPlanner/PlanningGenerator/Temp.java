package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.model.LogType;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class Temp implements PlanningGenerator {

//    @Autowired
//    private AvailabilityService availabilityService;
//
//    @Autowired
//    private WorkingDayService workingDayService;
//
//    @Autowired
//    private PlanningService planningService;
//
//    @Autowired
//    private StaffService staffService;


    @Override
    public Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays,
                                     AvailabilityService availabilityService, PlanningStatusService planningStatusService,
                                     LogDetailService logDetailService, DaysOfWeekRepository daysOfWeekRepository, StaffService staffService) {

        Response response = new Response();
        List<LogDetailDto> logging = new ArrayList<>();

        if (workingDays.size() != 7) {
            logDetailService.addLog(logging, "Week of WorkingDays not completely configured", LogType.INFO);
            return response;
        } else {
            logDetailService.addLog(logging, "Week of WorkingDays is complete", LogType.INFO);

            List<PlanningDto> planningList;
            List<DaysOffWeek> daysOffWeekList = sortWorkingDaysForComplexity(workingDays, daysOfWeekRepository);

            checkIfWeekAlreadyPlanned(workingDays, staff);

            planningList = populateShifts(workingDays, daysOffWeekList, staff, availabilityService, planningStatusService);
            response.setPlanningList(planningList);
        }
        staffService.saveAllStaff(staff);

        return response;
    }


    private List<PlanningDto> populateShifts(List<WorkingDayDto> workingDays, List<DaysOffWeek> daysOffWeekList,
                                             List<StaffDto> staff, AvailabilityService availabilityService,
                                             PlanningStatusService planningStatusService) {

        List<PlanningDto> planningList = new ArrayList<>();
        int hoursPerShift = 8;
        int adjustedHoursPerShift = hoursPerShift;

        for (DaysOffWeek dayOffWeek : daysOffWeekList) {
            //sorted on most complex days first
            WorkingDayDto day = getWorkingDayByDayOfWeek(workingDays, dayOffWeek);

            String planningStatus = "Planned NOK";
            List<PlanningDetailDto> shifts = new ArrayList<>();

            String planningStatusString = day.getPlanningDto().getPlanningStatusDto().getPlanningStatusName();
            if (day.isOpen() &&
                    (planningStatusString.equalsIgnoreCase("Not planned")
                            || planningStatusString.equalsIgnoreCase("Planned NOK"))){
                List<StaffDto> availableStaff = getAvailableStaff(day, new ArrayList<>(staff));

                //Get amount of shifts that needs to be planned
                int amountOfShifts = day.getMinimalOccupation();
                // if planned (manually) but not ok, get amount of shifts that still needs to be planned
                if (day.getPlanningDto().getPlanningStatusDto().getPlanningStatusName().equalsIgnoreCase("Planned NOK")) {
                    amountOfShifts -= day.getPlanningDto().getPlanningDetails().size();
                }

                for (int i = 0; i < amountOfShifts; i++) {
                    adjustedHoursPerShift = hoursPerShift;
                    PlanningDetailDto planningDetailDto = new PlanningDetailDto();
                    StaffDto planStaff = null;

                    //if not empty
                    if(! availableStaff.isEmpty()){
                        planStaff = availableStaff.getFirst();
                        planningStatus = "Planned OK";
                        //if too many hours in debt, make shifts longer
                        if (planStaff.getHoursInDebt() > 4) {
                            hoursPerShift += 1;
                        }

                        planStaff.setHoursInDebt(planStaff.getHoursInDebt() - adjustedHoursPerShift);

//                        planStaff.setHoursInDebt(planStaff.getHoursInDebt() - getShiftTime(planStaff, day));
                    }

                    planningDetailDto.setStaff(planStaff);

//                    int shiftTime = getShiftTime(planStaff, day);


//                        if((i + 1) % 3 == 0){
//                            // Midday shift
//                             long hoursNeeded = day.getStartTime().until(day.getEndTime(), ChronoUnit.HOURS);
//                        } else
                    if ((i) % 2 == 0) {
                        // Opening shift
                        planningDetailDto.setStartTime(day.getStartTime());
                        planningDetailDto.setEndTime(day.getStartTime().plusHours(adjustedHoursPerShift));
                    } else {
                        // Closing shift
                        planningDetailDto.setStartTime(day.getEndTime().minusHours(adjustedHoursPerShift));
                        planningDetailDto.setEndTime(day.getEndTime());
                    }

                    //if a staf is asigned, the shift is planned, if staff is null, the shift is an open shift
                    if(planningDetailDto.getStaff() != null){
                        planningDetailDto.setAvailability(availabilityService.getByAvailabilityType("Planned"));
                    } else {
                        planningDetailDto.setAvailability(availabilityService.getByAvailabilityType("OPEN SHIFT"));

                    }
                    availableStaff.remove(planStaff);
                    shifts.add(planningDetailDto);
                }
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

//    private int getShiftTime(StaffDto planStaff, WorkingDayDto day) {
//        int hoursPerShift = 8;
//
//        if(planStaff != null){
//            //if too many hours in debt, make shifts longer
//            if (planStaff.getHoursInDebt() > 4) {
//                hoursPerShift += 1;
//            }
//        }
//
//        int dayHours = (int) HOURS.between(day.getStartTime(), day.getEndTime());
//        if(dayHours <= 8){
//            hoursPerShift =  dayHours;
//        }
//
//        System.out.println(day.isOpen());
//        System.out.println("plannStaff. " + planStaff);
//        System.out.println("ShiftTime; " + hoursPerShift);
//        return hoursPerShift;
//
//    }


    //Sort list of workingDays so that most complex days (with most unavailable staff) are planned first
    private List<DaysOffWeek> sortWorkingDaysForComplexity(List<WorkingDayDto> workingDays, DaysOfWeekRepository daysOfWeekRepository) {
        List<DaysOffWeek> daysOffWeekList = new ArrayList<>();

        for (WorkingDayDto dayDto : workingDays) {
            DaysOffWeek daysOffWeek = daysOfWeekRepository.findByDayName(dayDto.getDate().getDayOfWeek().toString());
            daysOffWeekList.add(daysOffWeek);
        }

        //sort daysOfWeekList on amount of staff unavailable, then reverse to set days with most unavailable staff first
        daysOffWeekList.sort(Comparator.comparingInt(o -> o.getStaff().size()));
        Collections.reverse(daysOffWeekList);
        return daysOffWeekList;
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
            if (staffDto.getHoursInDebt() < 0
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
                if (staffDto.getStaffId() == planningDetailDto.getStaff().getStaffId()
                        && planningDetailDto.getAvailability().getAvailabilityId() > 1) {
                    //if already planned on this day, subtract hoursInDebt with duration of the shift
                    Duration duration = Duration.between(planningDetailDto.getStartTime(), planningDetailDto.getEndTime());
                    staffDto.setHoursInDebt((int) (staffDto.getHoursInDebt() - duration.toHours()));
                    notAvailableStaff.add(staffDto);
                    break;
                }
            }
        }
        //removes all unavailable staff
        availableStaff.removeAll(notAvailableStaff);

        //sorts the available staff so that staff with most hours in debt come first, then as a second factor they are
        //sorted by most hoursPerWeek in their contract
        availableStaff.sort(
                Comparator.comparingInt(StaffDto::getHoursInDebt)
                        .thenComparingInt(staff -> staff.getContractType().getHoursPerWeek())
        );

        Collections.reverse(availableStaff);

        return availableStaff;
    }


    //not a great method, probably better way to determine if a week is planned.
    // if a week has more than 3 days that are not planned, it is seen as an unplanned week, and thus
    // hoursInDebt for the staff are set by their contract hoursPerWeek
    private void checkIfWeekAlreadyPlanned(List<WorkingDayDto> workingDays, List<StaffDto> staff) {
        int counter = 0;
        for(WorkingDayDto workingDayDto: workingDays){
            String status = workingDayDto.getPlanningDto().getPlanningStatusDto().getPlanningStatusName();
            if(status.equalsIgnoreCase("Not planned")){
                counter++;
            }
        }
        if(counter > 3){
            setStaffHoursInDebt(staff, workingDays);
        }
    }


    //Added a field in StaffDto (hoursInDebt) to keep track of how many hours still needs to be planned in a week,
    //this is also for keeping track of plus minus hours
    private void setStaffHoursInDebt(List<StaffDto> staff, List<WorkingDayDto> workingDayDtos) {
        // TODO fixen dat staff altijd een contract moet hebben, anders begaait da de planning
        for (StaffDto staffDto : staff) {
            if(staffDto.getEndDate() == null){
                staffDto.setHoursInDebt(staffDto.getHoursInDebt() + staffDto.getContractType().getHoursPerWeek());
            } else {
                //Check if staff is still active
                if(!staffDto.getEndDate().isBefore(workingDayDtos.getFirst().getDate())){
                    staffDto.setHoursInDebt(staffDto.getHoursInDebt() + staffDto.getContractType().getHoursPerWeek());
                }
            }
        }
    }

//    //When a week already was planned, the planningList will be empty, so hoursInDebt has to be reset
//    private void resetStaffHoursInDebt(List<StaffDto> staff, LocalDate date) {
//        for (StaffDto staffDto : staff) {
//            if(staffDto.getEndDate() == null){
//                staffDto.setHoursInDebt(staffDto.getHoursInDebt() - staffDto.getContractType().getHoursPerWeek());
//            } else {
//                //Check if staff is still active
//                if(!staffDto.getEndDate().isBefore(date)){
//                    staffDto.setHoursInDebt(staffDto.getHoursInDebt() - staffDto.getContractType().getHoursPerWeek());
//                }
//            }
//        }
//    }

//    @Override
//    public Response generatePlanningV2(int weekNumber) {
////        System.out.println("starting");
////
////        List<WorkingDayDto> weekPlanning = workingDayService.getByWeekNumber(weekNumber);
////        List<StaffDto> allStaff = staffService.getAll();
////        Response response = new Response();
////
////        System.out.println("workingDays; " + weekPlanning);
////
////        Planner planner = new Planner();
//
//        return null;
//    }
}
