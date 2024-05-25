package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningStatusDto;

import java.util.List;

public interface PlanningStatusService {
    List<PlanningStatusDto> getAll();
    void save(PlanningStatusDto planningStatusDto);
    PlanningStatusDto findByPlanningStatusName(String planningStatusName);
    PlanningStatusDto findByPlanningStatusId(long planningStatusId);

}
