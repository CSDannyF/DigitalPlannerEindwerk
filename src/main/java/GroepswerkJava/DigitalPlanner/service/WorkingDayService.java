package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface WorkingDayService {

    @Transactional
    void updateWorkingDay(WorkingDay exceptionDay);

    List<WorkingDayDto> getAllDto();

    public void save(WorkingDay workingDay);

    public WorkingDay getById(long id);

    void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays);

    void saveAll(List<WorkingDay> workingDays);

    WorkingDayDto getByDate(LocalDate date);

    List<WorkingDay> getAll();

    List<WorkingDayDto> getByWeekNumber(int weekNumber);

    WorkingDayDto mapToDto(WorkingDay workingDay);
}


