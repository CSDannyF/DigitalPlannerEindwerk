package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.PlanningRepository;
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

    @Override
    public void save(Planning planning) {
        this.planningRepository.save(planning);
    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDay workingDay) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(workingDay));
    }

    private PlanningDto mapToPlanningDto(Planning planning){
        PlanningDto planningDto = new PlanningDto();
        if(planning != null){
            planningDto.setPlanningId(planning.getPlanningId());
            planningDto.setPlanningStatus(planning.getPlanningStatus().getPlanningStatusName());
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
        planningDetailDto.setAvailability(planningDetail.getAvailability().getAvailabilityType());
        return planningDetailDto;
    }
}
