package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.repository.PlanningDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanningDetailServiceImpl implements PlanningDetailService{

    @Autowired
    private PlanningDetailRepository planningDetailRepository;


    @Override
    public void save(PlanningDetail planningDetail) {
        planningDetailRepository.save(planningDetail);
    }

    @Override
    public PlanningDetail get(long id) {
        Optional<PlanningDetail> planningDetailOptional = planningDetailRepository.findById(id);
        return planningDetailOptional.orElse(null); // Return null if planningDetailOptional is empty, otherwise return the PlanningDetail object inside the Optional
    }

}
