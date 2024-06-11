package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ListPlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanningService {
    void save(PlanningDto planningDto);
    void update(Planning planning);

    void savePlanningDetails(PlanningDto planningDto, ListPlanningDetailDto listPlanningDetailDto, String plannedBy);

    void saveAll(List<PlanningDto> planningDtoList);
    PlanningDto getByWorkingDay(WorkingDayDto workingDayDto);

    boolean hasDuplicateStaff(List<PlanningDetailDto> planningDetails);
}
