package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PlanningGenerator {

    Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays);
}
