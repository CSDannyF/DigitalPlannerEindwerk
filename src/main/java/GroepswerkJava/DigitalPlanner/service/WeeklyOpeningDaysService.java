package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;

import java.util.List;

public interface WeeklyOpeningDaysService extends IService<WeeklyOpeningDay>{

    void saveAll(List<WeeklyOpeningDay> weeklyOpeningDays);

    void overWriteAll(List<WeeklyOpeningDay> weeklyOpeningDayList);

    int getTotalShiftHoursPerWeek();

}
