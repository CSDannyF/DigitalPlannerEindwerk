package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service("planner")
public class Planner implements PlanningGenerator {

    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningServiceImpl planningService;

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private LogDetailService logDetailService;

    @Autowired PlanningStatusService planningStatusService;

    private List<WorkingDay> workingDays = new ArrayList<>();

    @Override
    public Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays)
    {
        List<LogDetailDto> logging = new ArrayList<>();
        List<PlanningDto> planningList = new ArrayList<>();
        Response response = new Response();

        logDetailService.addLog(logging, "Checking if workingDays has 7 days.", LogType.INFO);
        if(workingDays.size() != 7)     //check If the 7 workingDays are configured
        {
            logDetailService.addLog(logging, "Week of workingDays not completely configured.", LogType.INFO);

            return response;
        } else {

            logDetailService.addLog(logging, "Week of workingDays is complete.", LogType.INFO);

            //Check if every WorkingDay has a corresponding Planning
            logDetailService.addLog(logging, "Checking if every workingDay has a corresponding Planning.", LogType.INFO);

            for (WorkingDayDto day: workingDays)
            {
                List<PlanningDetailDto> planningDetails = new ArrayList<>();
                PlanningDto planning = day.getPlanningDto();
                planning.setWorkingDayDto(day);

                planning.setPlannedDate(LocalDate.now());
                planning.setPlanningDetails(planningDetails);

                if(isDayPlannedAndOpen(planning)){
                    logDetailService.addLog(logging, "PlanningDay is not complete. \n" + "Start planning.", LogType.INFO);

                    LocalTime startTime = planning.getWorkingDayDto().getStartTime();
                    LocalTime endTime = planning.getWorkingDayDto().getEndTime();

                    //Set the total work hours for the day
                    logDetailService.addLog(logging, "Setting the parameters for the planningDay.", LogType.INFO);

                    int minOccupation = planning.getWorkingDayDto().getMinimalOccupation();
                    int openingsHours = endTime.getHour() - startTime.getHour();
                    int totalWorkHours = minOccupation * openingsHours;

                    //loop trough staffmembers to set de planningDetail
                    logDetailService.addLog(logging, "looping trough the staff members...", LogType.INFO);
                    for(StaffDto staff1 : staff)
                    {
                        //set staff to planningdetail and add to planning
                        PlanningDetailDto detail = new PlanningDetailDto();

                        //check if schedule for the day is full, if not  set work
                        logDetailService.addLog(logging, "Checking if plannning day" + planning.getWorkingDayDto().getDate() + "needs more staff members", LogType.INFO);
                        if(minOccupation > 0)
                        {
                            logDetailService.addLog(logging, "Insufficient staff for this day. \nSetting the startTime, endTime en workMinutes for staff member.", LogType.INFO);

                            detail.setStartTime(startTime);
                            detail.setEndTime(endTime);
                            detail.setStaff(staff1);
                            detail.setWorkMinutesPerDay(openingsHours*60); //todo moet worden veranderd naar uur?
                            detail.setAvailability(availabilityService.getByAvailabilityType("planned"));

                            totalWorkHours -= openingsHours;               //subtract openingshours/workinghours of totalWorkHours
                            minOccupation -= 1;                            //subtract employee of minOccupation
                        } else
                        {
                            logDetailService.addLog(logging, "Day is fully planned, no more additional staff required.", LogType.INFO);

                            detail.setWorkMinutesPerDay(0);
                            detail.setStaff(staff1);
                            planning.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned OK"));
                            detail.setAvailability(availabilityService.getByAvailabilityType("Available"));
                        }

                        //add planningDetail to planning
                        logDetailService.addLog(logging, "Adding planningDetail to the planning.", LogType.INFO);

                        planningDetails.add(detail);
                    }
                } else {
                    planning.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Closed"));
                }
                if (day.getMinimalOccupation() == day.getPlanningDto().getPlanningDetails().size()) {
                    planning.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned OK"));
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
}