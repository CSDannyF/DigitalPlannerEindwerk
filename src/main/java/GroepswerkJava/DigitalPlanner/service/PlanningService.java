package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PlanningService {
    void save(PlanningDto planningDto);
    void update(Planning planning);

    void savePlanningDetails(PlanningDto planningDto, ListPlanningDetailDto listPlanningDetailDto, String plannedBy);

    void saveAll(List<PlanningDto> planningDtoList);
    PlanningDto getByWorkingDay(WorkingDayDto workingDayDto);

    boolean hasDuplicateStaff(List<PlanningDetailDto> planningDetails);

    void saveUnavailabilities(LocalDate startDate, LocalDate endDate, Long unavailabilityId, long staffId);

    List<StaffDto> getAvailableStaff(List<PlanningDetailDto> planningDetails);

    PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail);

}
