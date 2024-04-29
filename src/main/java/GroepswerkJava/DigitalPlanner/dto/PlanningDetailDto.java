package GroepswerkJava.DigitalPlanner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningDetailDto {
    private long planningDetailId;
    private String staff;
    private String availability;
    private Time startTime;
    private Time endTime;

    @Override
    public String toString() {
        return "PlanningDetailDto{" +
                "Id=" + planningDetailId +
                ", staff=" + staff +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availability=" + availability +
                '}';
    }
}
