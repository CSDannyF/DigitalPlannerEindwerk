package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.service.AvailabilityService;
import GroepswerkJava.DigitalPlanner.service.PlanningStatusService;

import java.util.List;

public interface PlanningGenerator {
    Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays, AvailabilityService availabilityService, PlanningStatusService planningStatusService);
}
