package GroepswerkJava.DigitalPlanner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningDetailDto {
    private long planningDetailId;
    private StaffDto staff;
    private long staffId;
    private long planningId;
    private AvailabilityDto availability;
    private LocalTime startTime;
    private LocalTime endTime;
    private int workMinutesPerDay;



    @Override
    public int hashCode() {
        return Objects.hash(planningId, staffId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningDetailDto that = (PlanningDetailDto) o;
        return planningDetailId == that.planningDetailId &&
                staffId == that.staffId &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

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
