package GroepswerkJava.DigitalPlanner.dto;


import GroepswerkJava.DigitalPlanner.model.Staff;
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
//    private StaffDto staff;
    private String staff;
    private long staffId;

    private String availability;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return "PlanningDetailDto{" +
                "Id=" + planningDetailId +
                ", staff=" + staff +
                ", staffId=" + staffId +

                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availability=" + availability +
                '}';
    }
}
