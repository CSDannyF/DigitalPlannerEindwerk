package GroepswerkJava.DigitalPlanner.dto;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;

import java.util.ArrayList;
import java.util.List;

public class OpeningDaysDto {
    private List<WeeklyOpeningDay> openingDaysList;

    public OpeningDaysDto(){
        openingDaysList = new ArrayList<>();
    }

    public OpeningDaysDto(List<WeeklyOpeningDay> openingDaysList){
        this.openingDaysList = openingDaysList;
    }

    public List<WeeklyOpeningDay> getOpeningDaysList() {
        return openingDaysList;
    }

    public void setOpeningDaysList(List<WeeklyOpeningDay> openingDaysList) {
        this.openingDaysList = openingDaysList;
    }

    public void addOpeningDay(WeeklyOpeningDay weeklyOpeningDay){
        this.openingDaysList.add(weeklyOpeningDay);
    }
}
