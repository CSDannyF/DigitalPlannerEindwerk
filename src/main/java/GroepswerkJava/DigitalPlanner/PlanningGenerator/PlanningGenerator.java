package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.AvailabilityService;
import GroepswerkJava.DigitalPlanner.service.LogDetailService;
import GroepswerkJava.DigitalPlanner.service.PlanningStatusService;
import GroepswerkJava.DigitalPlanner.service.StaffService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PlanningGenerator {

    Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays);
}
