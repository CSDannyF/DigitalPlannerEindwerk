package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;

import java.util.List;


public interface PlanningService {
    void save(PlanningDto planningDto);
    PlanningDto getByWorkingDay(WorkingDayDto workingDayDto);
    void savePlanningList(List<PlanningDto> planningDtos);
}
