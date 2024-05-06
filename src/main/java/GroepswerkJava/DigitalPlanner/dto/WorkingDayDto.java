package GroepswerkJava.DigitalPlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkingDayDto {
    private long workingDayId;
    private LocalDate date;
    private boolean open;
    private LocalTime startTime;
    private int minimalOccupation;
    private LocalTime endTime;

    @Override
    public String toString() {
        return "WorkingDayDto{" +
                "workingDayId=" + workingDayId +
                ", date=" + date +
                ", open=" + open +
                ", startTime=" + startTime +
                ", minimalOccupation=" + minimalOccupation +
                ", endTime=" + endTime +
                '}';
    }
}
