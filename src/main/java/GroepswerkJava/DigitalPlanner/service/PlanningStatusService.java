package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.PlanningStatus;

public interface PlanningStatusService {
    PlanningStatus findByPlanningStatusName(String name);

}
