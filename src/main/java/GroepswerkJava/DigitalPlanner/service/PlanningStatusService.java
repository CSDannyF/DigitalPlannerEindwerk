package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningStatusDto;
import GroepswerkJava.DigitalPlanner.model.PlanningStatus;

import java.util.List;

public interface PlanningStatusService {
    List<PlanningStatusDto> getAll();
    void save(PlanningStatusDto planningStatusDto);
    PlanningStatus findByPlanningStatusName(String planningStatusName);

}
