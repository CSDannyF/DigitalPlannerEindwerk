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
    private long planningStatusId;
    private String plannedByStaff;
    private LocalDate plannedDate;
    private WorkingDayDto workingDayDto;
    private long workingDayDtoId;

    public void addPlanningDetail(PlanningDetailDto planningDetailDto){
        planningDetails.add(planningDetailDto);
    }

    @Override
    public String toString() {
        return "PlanningDto{" +
                "planningId=" + planningId +
                ", planningDetails=" + planningDetails +
                ", planningStatusId=" + planningStatusId +
                ", plannedByStaff='" + plannedByStaff + '\'' +
                ", plannedDate=" + plannedDate +
                ", workingDayDtoId=" + workingDayDtoId +

                '}';
    }
}
