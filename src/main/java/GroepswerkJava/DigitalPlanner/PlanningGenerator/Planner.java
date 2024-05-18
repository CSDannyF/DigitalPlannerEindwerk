package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.service.PlanningServiceImpl;
import GroepswerkJava.DigitalPlanner.service.PlanningStatusServiceImpl;
import GroepswerkJava.DigitalPlanner.service.StaffServiceImpl;
import GroepswerkJava.DigitalPlanner.service.WorkingDayService;
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
    private PlanningStatusServiceImpl planningStatusService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningServiceImpl planningService;

    @Autowired
    private StaffServiceImpl staffService;



    private List<Staff> staff = new ArrayList<>();
    private List<WorkingDay> workingDays = new ArrayList<>();
    private List<Planning> plannings = new ArrayList<>();

    @Override
    public Response generatePlanning(List<Staff> staff, List<WorkingDay> workingDays/*, List<Planning> plannings*/)
    {
        List<String> logging = new ArrayList<>();
        List<Planning> planningList = new ArrayList<>();
        Response response = new Response();

        logging.add("check if workingdays has 7 days");
        if(workingDays.size() != 7)     //check If the 7 workingDays are configured
        {
            logging.add("Week of WorkingDays not completely configured");
            return response;
        } else {

            logging.add("Week of WorkingDays is complete");

            //Check if every WorkingDay has a corresponding Planning
            logging.add("Checking if every workingDay has a corresponding Planning");
            //planningStatusService.getAll();

            planningList = FakeSetWorkingDayToPlanning(workingDays);        // als dit werkt moet de planning niet worden meegegeven in de methode want deze zit in de workingDays
            logging.add("Checked on corresponding workingDay done");

            for (Planning planning: planningList)
            {
                List<PlanningDetail> planningDetails = new ArrayList<>();
                planning.setPlannedDate(LocalDate.now());
                planning.setPlanningDetails(planningDetails);

                if(isDayPlanned(planning)){
                    logging.add("PlanningDay is not complete \n" + "Start planning");

                    LocalTime startTime = planning.getWorkingDay().getStartTime();
                    LocalTime endTime = planning.getWorkingDay().getEndTime();

                    //Set the total work hours for the day
                    logging.add("Setting the parameters for the planning day");
                    int minOccupation = planning.getWorkingDay().getMinimalOccupation();
                    int openingsHours = endTime.getHour() - startTime.getHour();
                    int totalWorkHours = minOccupation * openingsHours;

                    //loop trough staffmembers to set de planningDetail
                    logging.add("looping trough the staffmembers");
                    for(Staff staff1 : staff)
                    {
                        //set staff to planningdetail and add to planning
                        PlanningDetail detail = new PlanningDetail();

                        //detail.setAvailability();

                        //check if schedule for the day is full, if not  set work
                        logging.add("check if plannning day" + planning.getWorkingDay().getDate() + "needs more staff members");
                        if(minOccupation > 0)
                        {
                            logging.add("There need to be more staff for this day \nSetting the startTime, endTime en work minutes off the day for staff member " + staff1.getStaffFirstName());
                            detail.setStartTime(startTime);
                            detail.setEndTime(endTime);
                            detail.setStaff(staff1);
                            detail.setWorkMinutesPerDay(openingsHours*60); //todo moet worden veranderd naar uur?
                            totalWorkHours -= openingsHours;               //subtract openingshours/workinghours of totalWorkHours
                            minOccupation -= 1;                            //subtract employee of minOccupation
                        } else
                        {
                            logging.add("The day is fully planned, no more staff needed");
                            detail.setWorkMinutesPerDay(0);
                            detail.setStaff(staff1);
                        }

                        //add planningDetail to planning
                        logging.add("adding planningDetail to the planning");
                        planningDetails.add(detail);
                    }
                }
            }
        }
        response.setPlannings(planningList);
        response.setLogging(logging);
        return response;
    }

    //Method to check if there is already a Planning linked to the workingDay, if not: the workingDay wil be set to an
    //new planning object corresponding to the workingDay object

    //de datum van het planning object en workingday object moeten hier nog hetzelfde gemaakt worden
    public List<Planning> setWorkingDayToPlanning(List<WorkingDay> workingDays)
    {
        PlanningStatus planningStatus = planningStatusService.findByPlanningStatusName("Not planned");

        for(WorkingDay workingDay: workingDays)
        {
            if(workingDay.getPlanning() == null)
            {
                Planning planning = new Planning();
                planning.setWorkingDay(workingDay);
                planning.setPlanningStatus(planningStatus);
                plannings.add(planning);
            } else {
                plannings.add(workingDay.getPlanning());
            }
        }
        return plannings;
    }


    //check if day is planned
    private boolean isDayPlanned(Planning planning)
    {
        if(planning.getPlanningStatus().getPlanningStatusId() <= 2)
        {
            return planning.getWorkingDay().isOpen();
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

    //deze wordt voorlopig gebruikt in de generate() omdat mijn services null zijn
    public List<Planning> FakeSetWorkingDayToPlanning(List<WorkingDay> workingDays)
    {
        PlanningStatus planningStatus = new PlanningStatus();
        planningStatus.setPlanningStatusName("Not planned");

        for(WorkingDay workingDay: workingDays)
        {
            if(workingDay.getPlanning() == null)
            {
                Planning planning = new Planning();
                planning.setWorkingDay(workingDay);
                planning.setPlanningStatus(planningStatus);
                plannings.add(planning);
            } else {
                plannings.add(workingDay.getPlanning());
            }
        }
        return plannings;
    }


    //plannen met services maar krijg hier nullpointerfout op
    public void plan() {

        Planner planner = new Planner();

        List<StaffDto> staffDtos = new ArrayList<>();
        List<Staff> staffList = new ArrayList<>();
        for (StaffDto staffDto : staffDtos) {
            Staff staff = staffService.mapToStaffModel(staffDto);
            staffList.add(staff);
        }


        List<WorkingDay> workingDay = new ArrayList<>();
        LocalDate date = LocalDate.of(2024,7,1);
        for(int i = 0; i < 7; i++) {
            WorkingDayDto workingDayDto = workingDayService.getByDate(date);

            workingDay.add(planningService.mapFromWorkingDayDto(workingDayDto));
            date = date.plusDays(1);
        }

        List<Planning> plannings= new ArrayList<>();
/*
        for(WorkingDay workingDay1 : workingDay) {
            workingDayService.getAllDto()

            planningService.getByWorkingDay()
            plannings.add()
        }

 */
        Response response = planner.generatePlanning(staffList,workingDay);
    }
}
