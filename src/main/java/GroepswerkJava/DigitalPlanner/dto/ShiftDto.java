package GroepswerkJava.DigitalPlanner.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class ShiftDto {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;


    @Override
    public String toString() {
        return "ShiftDto{" +
                "date=" + date +
                ", startTime=" + startTime +
                ", endtime=" + endTime +
                '}';
    }
}
