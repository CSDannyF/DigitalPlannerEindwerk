package GroepswerkJava.DigitalPlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private int totalStaffContractHours;
    private int totalWorkingHoursNeeded;
    private  int percentage;

}
