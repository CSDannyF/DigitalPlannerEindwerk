package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningStatusDto;
import GroepswerkJava.DigitalPlanner.model.PlanningStatus;

import java.util.List;

public interface PlanningStatusService {
    List<PlanningStatusDto> getAll();

    PlanningStatusDto save(PlanningStatusDto planningStatusDto);

    PlanningStatusDto findByPlanningStatusName(String planningStatusName);

    PlanningStatusDto findByPlanningStatusId(long planningStatusId);

    PlanningStatus findByStatusName(String statusName);

    PlanningStatusDto mapToPlanningStatusDto(PlanningStatus planningStatus);

    PlanningStatus mapToPlanningStatus(PlanningStatusDto planningStatusDto);
}
