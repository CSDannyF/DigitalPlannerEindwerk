package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.PlanningDetailRepository;
import GroepswerkJava.DigitalPlanner.repository.PlanningRepository;
import GroepswerkJava.DigitalPlanner.repository.PlanningStatusRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class PlanningServiceImpl implements PlanningService {

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private PlanningDetailRepository planningDetailRepository;

    @Autowired
    private PlanningStatusRepository planningStatusRepository;

    @Autowired
    StaffService staffService;

    @Override
    public void save(PlanningDto planningDto) {
        //TODO implementeer methode
        //this.planningRepository.save(planningDto);
    }

    @Override
    public void saveAll(@NonNull List<PlanningDto> planningDtoList) {

    }


    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }


    //Dto mappers
    private Planning mapToPlanning(PlanningDto planningDto) {
        Planning planning = new Planning();

        planning.setPlanningId(planningDto.getPlanningId());
        planning.setPlannedByStaff(planningDto.getPlannedByStaff());
        planning.setPlannedDate(planningDto.getPlannedDate());
        planning.setWorkingDay(mapFromWorkingDayDto(planningDto.getWorkingDayDto()));

        // Convert and set PlanningDetail list
        List<PlanningDetail> planningDetails = planningDto.getPlanningDetails().stream()
                .map(detailDto -> {
                    PlanningDetail detail = new PlanningDetail();
                    detail.setPlanningDetailId(detailDto.getPlanningDetailId());
                    detail.setStartTime(detailDto.getStartTime());
                    detail.setEndTime(detailDto.getEndTime());
                    detail.setPlanning(planning);
                    return detail;
                }).collect(Collectors.toList());

//        List<PlanningDetail> planningDetails = new ArrayList<>();
        planning.setPlanningDetails(planningDetails);
        return planning;
    }

    private WorkingDay mapFromWorkingDayDto(WorkingDayDto workingDayDto) {
        WorkingDay workingDay = new WorkingDay();
        workingDay.setWorkingDayId(workingDayDto.getWorkingDayId());
        workingDay.setDate(workingDayDto.getDate());
        workingDay.setOpen(workingDayDto.isOpen());
        workingDay.setMinimalOccupation(workingDayDto.getMinimalOccupation());
        workingDay.setStartTime(workingDayDto.getStartTime());
        workingDay.setEndTime(workingDayDto.getEndTime());
        return workingDay;
    }

    private PlanningDto mapToPlanningDto(Planning planning) {
        PlanningDto planningDto = new PlanningDto();
        if (planning != null) {
            planningDto.setPlanningId(planning.getPlanningId());
            if (planning.getPlanningStatus() != null) {
                planningDto.setPlanningStatus(planning.getPlanningStatus().getPlanningStatusName());
            }
            planningDto.setPlannedByStaff(planning.getPlannedByStaff());
            planningDto.setPlannedDate(planning.getPlannedDate());
            List<PlanningDetailDto> planningDetailDtoList = new ArrayList<>();
            for (PlanningDetail planningDetail : planning.getPlanningDetails()) {
                PlanningDetailDto planningDetailDto = mapToPlanningDetailDto(planningDetail);
                planningDetailDtoList.add(planningDetailDto);
            }
            planningDto.setPlanningDetails(planningDetailDtoList);
        } else {
            planningDto.setPlanningStatus("np");
        }
        return planningDto;
    }

    public PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail) {
        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());
        planningDetailDto.setStartTime(planningDetail.getStartTime());
        planningDetailDto.setEndTime(planningDetail.getEndTime());
        if(planningDetail.getStaff() != null){
            planningDetailDto.setStaffId(planningDetail.getStaff().getStaffId());
        }

        if(planningDetail.getStaff() != null){
            planningDetailDto.setStaff(planningDetail.getStaff().getStaffFirstName());
        }
        if (planningDetail.getAvailability() != null) {
            planningDetailDto.setAvailability(planningDetail.getAvailability().getAvailabilityType());
        }
        return planningDetailDto;
    }
}
