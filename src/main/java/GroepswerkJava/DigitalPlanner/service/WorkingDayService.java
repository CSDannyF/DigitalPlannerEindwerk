package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface WorkingDayService  {

    @Transactional
    void updateWorkingDay(WorkingDay exceptionDay);

    public void save(WorkingDay workingDay);
    public WorkingDay getById(long id);

    void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays);


    void saveAll(List<WorkingDay> workingDays);

    WorkingDay getByDate(LocalDate date);

    List<WorkingDay> getAll();
}
