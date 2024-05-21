package GroepswerkJava.DigitalPlanner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningDetailDto {
    private long planningDetailId;
    private StaffDto staff;
    private AvailabilityDto availability;
    private LocalTime startTime;
    private LocalTime endTime;
    private int workMinutesPerDay;

    @Override
    public String toString() {
        return "PlanningDetailDto{" +
                "Id=" + planningDetailId +
                ", staff=" + staff +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", workMinutesPerDay=" + workMinutesPerDay +
                ", availability=" + availability +
                '}';
    }
}
