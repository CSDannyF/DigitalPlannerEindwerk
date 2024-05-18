package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;

import java.util.List;

public interface PlanningGenerator {
    Response generatePlanning(List<Staff> staff, List<WorkingDay> workingDays);
}
