package GroepswerkJava.DigitalPlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanningDto {
    private long planningId;
    private List<PlanningDetailDto> planningDetails;
    private PlanningStatusDto planningStatusDto;
    private String plannedByStaff;
    private LocalDate plannedDate;
    private WorkingDayDto workingDayDto;

    @Override
    public String toString() {
        return "PlanningDto{" +
                "planningId=" + planningId +
                ", planningDetails=" + planningDetails +
                ", planningStatus=" + planningStatusDto +
                ", plannedByStaff='" + plannedByStaff + '\'' +
                ", plannedDate=" + plannedDate +
                '}';
    }
}
