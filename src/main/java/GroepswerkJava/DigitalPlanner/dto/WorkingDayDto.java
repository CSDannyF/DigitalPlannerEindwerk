package GroepswerkJava.DigitalPlanner.dto;

import GroepswerkJava.DigitalPlanner.model.Planning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkingDayDto {
    private long workingDayId;
    private LocalDate date;
    private int weekNumber;
    private boolean open;
    private LocalTime startTime;
    private int minimalOccupation;
    private PlanningDto planningDto;
    private LocalTime endTime;

    private DayOfWeek dayOfWeek;

    public WorkingDayDto(LocalDate date){
        this.date = date;
        dayOfWeek = date.getDayOfWeek();
    }



    @Override
    public String toString() {
        return "WorkingDayDto{" +
                "workingDayId=" + workingDayId +
                ", date=" + date +
                ", open=" + open +
                ", startTime=" + startTime +
                ", minimalOccupation=" + minimalOccupation +
                ", endTime=" + endTime +
                ", PLANNING=" + planningDto +

                '}';
    }
}
