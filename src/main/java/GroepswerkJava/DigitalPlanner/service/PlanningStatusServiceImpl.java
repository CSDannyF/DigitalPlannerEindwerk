package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.PlanningStatus;
import GroepswerkJava.DigitalPlanner.repository.PlanningStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanningStatusServiceImpl implements PlanningStatusService{

    @Autowired
    PlanningStatusRepository planningStatusRepository;

    @Override
    public PlanningStatus findByPlanningStatusName(String name) {
        return planningStatusRepository.findByPlanningStatusName(name);
    }
}
