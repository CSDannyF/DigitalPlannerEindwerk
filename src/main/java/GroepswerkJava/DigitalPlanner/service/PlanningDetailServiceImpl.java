package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.repository.PlanningDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class PlanningDetailServiceImpl implements PlanningDetailService{

    @Autowired
    private PlanningDetailRepository planningDetailRepository;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private PlanningStatusService planningStatusService;

    @Autowired
    private PlanningServiceImpl planningService;

    @Override
    public PlanningDetailDto save(PlanningDetail planningDetail) {
        PlanningDetail newPlanningDetail = planningDetailRepository.save(planningDetail);
        return planningService.mapToPlanningDetailDto(newPlanningDetail);
    }

    @Override
    public PlanningDetail get(long id) {
        Optional<PlanningDetail> planningDetailOptional = planningDetailRepository.findById(id);
        return planningDetailOptional.orElse(null); // Return null if planningDetailOptional is empty, otherwise return the PlanningDetail object inside the Optional
    }

    @Override
    public boolean assignOpenShift(Long shiftId, String email) {
        Staff staff = userLoginService.getStaffByUserEmail(email);
        PlanningDetail planningDetail = get(shiftId);
        Planning planning = planningDetail.getPlanning();
        boolean canBeAssigned = true;
        int openShiftCounter = 0;

        for(PlanningDetail detail: planning.getPlanningDetails()){
            if(detail.getStaff() == null){
                openShiftCounter++;
            } else if(detail.getStaff().getStaffId() == staff.getStaffId()){
                canBeAssigned = false;
            }
        }

        if(canBeAssigned){
            planningDetail.setStaff(staff);
            planningDetail.setAvailability(availabilityService.mapToAvailabilityModel(availabilityService.getByAvailabilityType("Planned")));

            int hours = (int) Duration.between(planningDetail.getStartTime(), planningDetail.getEndTime()).toHours();
            staff.setHoursInDebt(staff.getHoursInDebt() - hours);
            if(openShiftCounter <= 1){
                planning.setPlanningStatus(planningStatusService.findByStatusName("Planned OK"));
                planningService.update(planning);
            }
            save(planningDetail);
        }

        return canBeAssigned;
    }

}
