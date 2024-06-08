package GroepswerkJava.DigitalPlanner.dto;

import lombok.*;

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
                ", planningStatusId=" + planningStatusId +
                ", planningStatus=" + planningStatusDto +

                ", planningDetails=" + planningDetails +
//                ", plannedByStaff='" + plannedByStaff + '\'' +
//                ", plannedDate=" + plannedDate +
//                ", workingDayDto=" + workingDayDto +

                '}';
    }
}
