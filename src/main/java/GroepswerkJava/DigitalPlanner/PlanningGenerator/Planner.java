package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class Planner implements PlanningGenerator {

    @Autowired
    private AvailabilityServiceImpl availabilityService;  //geeft aan dat repo null is bij aanroepen, doet dit bij alle repos in deze klasse damn son

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningServiceImpl planningService;

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private LogDetailService logDetailService;

    private List<WorkingDay> workingDays = new ArrayList<>();  //voor de dummy data

    @Override
    public Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays, AvailabilityService availabilityService,
                                     PlanningStatusService planningStatusService,
                                     LogDetailService logDetailService, DaysOfWeekRepository daysOfWeekRepository, StaffService staffService)
    {
        List<LogDetailDto> logging = new ArrayList<>();
        List<PlanningDto> planningList = new ArrayList<>();
        Response response = new Response();

        logDetailService.addLog(logging, "check if workingdays has 7 days", LogType.INFO);
//        logging.add("check if workingdays has 7 days");
        if(workingDays.size() != 7)     //check If the 7 workingDays are configured
        {
            logDetailService.addLog(logging, "Week of WorkingDays not completely configured", LogType.INFO);

//            logging.add("Week of WorkingDays not completely configured");
            return response;
        } else {

            logDetailService.addLog(logging, "Week of WorkingDays is complete", LogType.INFO);
//            logging.add("Week of WorkingDays is complete");

            //Check if every WorkingDay has a corresponding Planning
            logDetailService.addLog(logging, "Checking if every workingDay has a corresponding Planning", LogType.INFO);
//            logging.add("Checking if every workingDay has a corresponding Planning");
            //planningStatusService.getAll();

            //planningList = setWorkingDayToPlanning(workingDays);        // als dit werkt moet de planning niet worden meegegeven in de methode want deze zit in de workingDays
            //logging.add("Checked on corresponding workingDay done");

            for (WorkingDayDto day: workingDays)
            {
                List<PlanningDetailDto> planningDetails = new ArrayList<>();
                PlanningDto planning = day.getPlanningDto();
                planning.setWorkingDayDto(day);

                planning.setPlannedDate(LocalDate.now());
                planning.setPlanningDetails(planningDetails);

                if(isDayPlannedAndOpen(planning)){
                    logDetailService.addLog(logging, "PlanningDay is not complete \n" + "Start planning", LogType.INFO);

//                    logging.add("PlanningDay is not complete \n" + "Start planning");

                    LocalTime startTime = planning.getWorkingDayDto().getStartTime();
                    LocalTime endTime = planning.getWorkingDayDto().getEndTime();

                    //Set the total work hours for the day
                    logDetailService.addLog(logging, "Setting the parameters for the planning day", LogType.INFO);

//                    logging.add("Setting the parameters for the planning day");
                    int minOccupation = planning.getWorkingDayDto().getMinimalOccupation();
                    int openingsHours = endTime.getHour() - startTime.getHour();
                    int totalWorkHours = minOccupation * openingsHours;

                    //loop trough staffmembers to set de planningDetail
                    logDetailService.addLog(logging, "looping trough the staffmembers", LogType.INFO);
//                    logging.add("looping trough the staffmembers");
                    for(StaffDto staff1 : staff)
                    {
                        //set staff to planningdetail and add to planning
                        PlanningDetailDto detail = new PlanningDetailDto();


                        //check if schedule for the day is full, if not  set work
                        logDetailService.addLog(logging, "check if plannning day" + planning.getWorkingDayDto().getDate() + "needs more staff members", LogType.INFO);
//                        logging.add("check if plannning day" + planning.getWorkingDayDto().getDate() + "needs more staff members");
                        if(minOccupation > 0)
                        {
                            logDetailService.addLog(logging, "There need to be more staff for this day \nSetting the startTime, endTime en work minutes off the day for staff member ", LogType.INFO);

//                            logging.add("There need to be more staff for this day \nSetting the startTime, endTime en work minutes off the day for staff member " + staff1.getFirstName());
                            detail.setStartTime(startTime);
                            detail.setEndTime(endTime);
                            detail.setStaff(staff1);
                            detail.setWorkMinutesPerDay(openingsHours*60); //todo moet worden veranderd naar uur?
                            detail.setAvailability(availabilityService.getByAvailabilityType("planned"));

                            totalWorkHours -= openingsHours;               //subtract openingshours/workinghours of totalWorkHours
                            minOccupation -= 1;                            //subtract employee of minOccupation
                        } else
                        {
                            logDetailService.addLog(logging, "The day is fully planned, no more staff needed", LogType.INFO);

//                            logging.add("The day is fully planned, no more staff needed");
                            detail.setWorkMinutesPerDay(0);
                            detail.setStaff(staff1);
                            planning.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned OK"));


//                            detail.setAvailability(availabilityService.getByAvailabilityType("Absence"));

                            detail.setAvailability(availabilityService.getByAvailabilityType("Available"));

                        }

                        //add planningDetail to planning
                        logDetailService.addLog(logging, "adding planningDetail to the planning", LogType.INFO);

//                        logging.add("adding planningDetail to the planning");
                        planningDetails.add(detail);
                    }
                } else {
                    planning.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Closed"));
                }
                planningList.add(planning);
            }
        }
        response.setPlanningList(planningList);
        response.setLogging(logging);
        response.saveLogsToDatabase(logDetailService);
        return response;
    }




    //check if day is planned
    private boolean isDayPlannedAndOpen(PlanningDto planning)
    {
        if(planning.getPlanningStatusDto().getPlanningStatusId() <= 2)
        {
            return planning.getWorkingDayDto().isOpen();
        }
        else {
            return false;
        }
    }

    //dummy data staff
    public List<Staff> createStaff()
    {
        ContractType contractType = new ContractType();
        contractType.setHoursPerWeek(40);
        contractType.setContractTypeName("Fulltime");
        List<Staff> staffList = new ArrayList<>();

        for(int i = 0; i<5; i++) {
            Staff staff1 = new Staff();
            staff1.setStaffFirstName("Jhon" + i);
            staff1.setContractType(contractType);
            staffList.add(staff1);
        }
        return staffList;
    }


    //dummy data workingdays
    public List<WorkingDay> createWorkingDays()
    {
        LocalDate date = LocalDate.of(2024,7,1);
        LocalTime startTime = LocalTime.of(9,0,0);
        LocalTime endTime= LocalTime.of(17,0,0);

        WorkingDay workingDay1 = new WorkingDay();
        workingDay1.setOpen(true);
        workingDay1.setStartTime(startTime);
        workingDay1.setEndTime(endTime);
        workingDay1.setMinimalOccupation(2);
        workingDay1.setDate(date.plusDays(1));

        WorkingDay workingDay2 = new WorkingDay();
        workingDay2.setOpen(true);
        workingDay2.setStartTime(startTime);
        workingDay2.setEndTime(endTime);
        workingDay2.setMinimalOccupation(3);
        workingDay2.setDate(date.plusDays(2));

        WorkingDay workingDay3 = new WorkingDay();
        workingDay3.setOpen(true);
        workingDay3.setStartTime(startTime);
        workingDay3.setEndTime(endTime);
        workingDay3.setMinimalOccupation(2);
        workingDay3.setDate(date.plusDays(3));

        WorkingDay workingDay4 = new WorkingDay();
        workingDay4.setOpen(true);
        workingDay4.setStartTime(startTime);
        workingDay4.setEndTime(endTime);
        workingDay4.setMinimalOccupation(3);
        workingDay4.setDate(date.plusDays(4));

        WorkingDay workingDay5 = new WorkingDay();
        workingDay5.setOpen(true);
        workingDay5.setStartTime(startTime);
        workingDay5.setEndTime(endTime);
        workingDay5.setMinimalOccupation(2);
        workingDay5.setDate(date.plusDays(5));

        WorkingDay workingDay6 = new WorkingDay();
        workingDay6.setOpen(false);
        workingDay6.setMinimalOccupation(2);
        workingDay6.setDate(date.plusDays(6));

        WorkingDay workingDay7 = new WorkingDay();
        workingDay7.setOpen(false);
        workingDay7.setMinimalOccupation(2);
        workingDay7.setDate(date.plusDays(7));

        workingDays.add(workingDay1);
        workingDays.add(workingDay2);
        workingDays.add(workingDay3);
        workingDays.add(workingDay4);
        workingDays.add(workingDay5);
        workingDays.add(workingDay6);
        workingDays.add(workingDay7);
        return workingDays;
    }

    //Method to check if there is already a Planning linked to the workingDay, if not: the workingDay wil be set to an
    //new planning object corresponding to the workingDay object

    //de datum van het planning object en workingday object moeten hier nog hetzelfde gemaakt worden
    /*
    public List<PlanningDto> setWorkingDayToPlanning(List<WorkingDayDto> workingDays)
    {
        planningStatusServiceImpl.getAll();
        PlanningStatusDto planningStatus = planningStatusServiceImpl.findByPlanningStatusName("Not planned");

        for(WorkingDayDto workingDay: workingDays)
        {
            if(workingDay.getPlanningDto() == null)
            {
                PlanningDto planning = new PlanningDto();
                planning.setWorkingDayDto(workingDay);
                planning.setPlanningStatusDto(planningStatus);
                plannings.add(planning);
            } else {
                plannings.add(workingDay.getPlanningDto());
            }
        }
        return plannings;
    }
     */


    @Override
    public Response generatePlanningV2(List<StaffDto> staff, List<WorkingDayDto> workingDays) {
        return null;
    }

}
