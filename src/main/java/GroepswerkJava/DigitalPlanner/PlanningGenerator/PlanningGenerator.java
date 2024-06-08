package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.PlanningGenerator.Response;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanningGenerator {
    Response generatePlanning(List<StaffDto> staff, List<WorkingDayDto> workingDays,
                              AvailabilityService availabilityService, PlanningStatusService planningStatusService,
                              LogDetailService logDetailService);

    Response generatePlanningV2(int weekNumber);
}
