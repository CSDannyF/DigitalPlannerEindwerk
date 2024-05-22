package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.PlanningRepository;
import GroepswerkJava.DigitalPlanner.repository.PlanningStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class PlanningServiceImpl implements PlanningService{

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Autowired
    private PlanningStatusRepository planningStatusRepository;

    @Override
    public void save(PlanningDto planningDto) {
        //TODO implementeer methode
        //this.planningRepository.save(planningDto);
    }

    @Override
    public void saveAll(List<PlanningDto> planningDtoList) {

    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }



    //Dto mappers

    public WorkingDay mapFromWorkingDayDto(WorkingDayDto workingDayDto){    //effe op public, stond op private
        WorkingDay workingDay = new WorkingDay();
        workingDay.setWorkingDayId(workingDayDto.getWorkingDayId());
        workingDay.setDate(workingDayDto.getDate());
        workingDay.setOpen(workingDayDto.isOpen());
        workingDay.setMinimalOccupation(workingDayDto.getMinimalOccupation());
        workingDay.setStartTime(workingDayDto.getStartTime());
        workingDay.setEndTime(workingDayDto.getEndTime());
        return workingDay;
    }

    protected PlanningDto mapToPlanningDto(Planning planning){
        PlanningDto planningDto = new PlanningDto();
        PlanningStatusServiceImpl planningStatusService = new PlanningStatusServiceImpl(planningStatusRepository);

        if(planning != null){

            planningDto.setPlanningId(planning.getPlanningId());
            if(planning.getPlanningStatus() != null){

                //map to planningStatusDto
                PlanningStatusDto planningStatusDto = planningStatusService.mapToPlanningStatusDto(planning.getPlanningStatus());
                planningDto.setPlanningStatusDto(planningStatusDto);
            }
            planningDto.setPlannedByStaff(planning.getPlannedByStaff());
            planningDto.setPlannedDate(planning.getPlannedDate());
            List<PlanningDetailDto> planningDetailDtoList = new ArrayList<>();
            for(PlanningDetail planningDetail: planning.getPlanningDetails()){
                PlanningDetailDto planningDetailDto = mapToPlanningDetailDto(planningDetail);
                planningDetailDtoList.add(planningDetailDto);
            }
            planningDto.setPlanningDetails(planningDetailDtoList);
        } else {
            planningDto.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Not planned"));
        }
        return planningDto;
    }

    public PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail){
        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());
        planningDetailDto.setStartTime(planningDetail.getStartTime());
        planningDetailDto.setEndTime(planningDetail.getEndTime());
        planningDetailDto.setWorkMinutesPerDay(planningDetail.getWorkMinutesPerDay());

        StaffDto staffDto = staffService.mapToStaffDto(planningDetail.getStaff());
        planningDetailDto.setStaff(staffDto);

        if(planningDetail.getAvailability() != null){
            AvailabilityDto availabilityDto = availabilityService.mapToAvailabilityDto(planningDetail.getAvailability());
            planningDetailDto.setAvailability(availabilityDto);
        }
        return planningDetailDto;
    }
}
