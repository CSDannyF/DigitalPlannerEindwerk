package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;


public interface PlanningService {
    void save(Planning planning);
    PlanningDto getByWorkingDay(WorkingDay workingDay);
}
