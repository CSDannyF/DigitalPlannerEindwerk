package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;

import java.util.List;

public interface WeeklyOpeningDaysService extends IService<WeeklyOpeningDay>{

    List<WeeklyOpeningDay> saveAll(List<WeeklyOpeningDay> weeklyOpeningDays);

    List<WeeklyOpeningDay> overWriteAll(List<WeeklyOpeningDay> weeklyOpeningDayList);

    int getTotalShiftHoursPerWeek();

}
