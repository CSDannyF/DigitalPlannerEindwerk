package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.PlanningDetailRepository;
import GroepswerkJava.DigitalPlanner.repository.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class PlanningServiceImpl implements PlanningService{

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private PlanningDetailRepository planningDetailRepository;

    @Override
    public void save(PlanningDto planningDto) {
        //TODO implementeer methode
        //this.planningRepository.save(planningDto);
    }

    @Override
    public void saveAll(List<PlanningDto> planningDtoList) {
        if (planningDtoList != null) {
            for (PlanningDto planningDto : planningDtoList) {
                System.out.println("workingday; " + planningDto.getWorkingDayDto());

                Planning planning = mapToPlanning(planningDto);
                planningRepository.save(planning);

                for (PlanningDetail detail : planning.getPlanningDetails()) {
                    planningDetailRepository.save(detail);
                }
            }
        } else {
            throw new IllegalArgumentException("planningDtoList cannot be null");
        }
    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }



    //Dto mappers

    private Planning mapToPlanning(PlanningDto planningDto) {
        Planning planning = new Planning();
        // set properties from planningDto to planning
        // you might need to convert DTO properties to entity properties
        planning.setPlannedByStaff(planningDto.getPlannedByStaff());
        planning.setPlannedDate(planningDto.getPlannedDate());
        planning.setWorkingDay(mapFromWorkingDayDto(planningDto.getWorkingDayDto())); // convert WorkingDayDto to WorkingDay if necessary

        // Convert and set PlanningDetail list
        List<PlanningDetail> planningDetails = planningDto.getPlanningDetails().stream()
                .map(detailDto -> {
                    PlanningDetail detail = new PlanningDetail();
                    // set properties from detailDto to detail
                    detail.setStartTime(detailDto.getStartTime());
                    detail.setEndTime(detailDto.getEndTime());
                    detail.setPlanning(planning);
                    // set other properties if necessary
                    return detail;
                }).collect(Collectors.toList());

        planning.setPlanningDetails(planningDetails);
        return planning;
    }

    private WorkingDay mapFromWorkingDayDto(WorkingDayDto workingDayDto){
        WorkingDay workingDay = new WorkingDay();
        workingDay.setWorkingDayId(workingDayDto.getWorkingDayId());
        workingDay.setDate(workingDayDto.getDate());
        workingDay.setOpen(workingDayDto.isOpen());
        workingDay.setMinimalOccupation(workingDayDto.getMinimalOccupation());
        workingDay.setStartTime(workingDayDto.getStartTime());
        workingDay.setEndTime(workingDayDto.getEndTime());
        return workingDay;
    }

    private PlanningDto mapToPlanningDto(Planning planning){
        PlanningDto planningDto = new PlanningDto();
        if(planning != null){
            planningDto.setPlanningId(planning.getPlanningId());
            if(planning.getPlanningStatus() != null){
                planningDto.setPlanningStatus(planning.getPlanningStatus().getPlanningStatusName());
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
            planningDto.setPlanningStatus("np");
        }
        return planningDto;
    }

    public PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail){
        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());
        planningDetailDto.setStartTime(planningDetail.getStartTime());
        planningDetailDto.setEndTime(planningDetail.getEndTime());
        planningDetailDto.setStaff(planningDetail.getStaff().getStaffFirstName());
        if(planningDetail.getAvailability() != null){
            planningDetailDto.setAvailability(planningDetail.getAvailability().getAvailabilityType());
        }
        return planningDetailDto;
    }
}
