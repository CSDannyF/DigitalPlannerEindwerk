package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface WorkingDayService {

    @Transactional
    void updateWorkingDay(WorkingDay exceptionDay);

    List<WorkingDayDto> getAllDto();

    WorkingDayDto save(WorkingDay workingDay);

    public WorkingDay getById(long id);

    void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays);

    List<WorkingDayDto> saveAll(List<WorkingDay> workingDays);

    WorkingDayDto getByDate(LocalDate date);

    WorkingDay getModelByDate(LocalDate date);


    List<WorkingDay> getAll();

    List<WorkingDayDto> getByWeekNumber(int weekNumber);

    WorkingDayDto mapToDto(WorkingDay workingDay);

}


