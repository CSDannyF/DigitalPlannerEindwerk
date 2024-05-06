package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;


public interface PlanningService {
    void save(PlanningDto planningDto);
    PlanningDto getByWorkingDay(WorkingDayDto workingDayDto);
}
