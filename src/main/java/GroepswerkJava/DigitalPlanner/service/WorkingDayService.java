package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import jakarta.transaction.Transactional;

import java.util.List;

public interface WorkingDayService extends IService<WorkingDay> {

    @Transactional
    void updateWorkingDay(WorkingDay exceptionDay);

    void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays);


    void saveAll(List<WorkingDay> workingDays);
}
