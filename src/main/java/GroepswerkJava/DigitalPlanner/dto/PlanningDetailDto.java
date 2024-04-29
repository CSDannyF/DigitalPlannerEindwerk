package GroepswerkJava.DigitalPlanner.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
public class PlanningDetailDto {
    private long planningDetailId;
    private PlanningDto planning;   //todo Create PlanningDto
    private StaffDto staff;
    private AvailabilityDto availability;   //todo Create AvailabilityDto
    private Time startTime;
    private Time endTime;
    private int workMinutesPerDay;
}