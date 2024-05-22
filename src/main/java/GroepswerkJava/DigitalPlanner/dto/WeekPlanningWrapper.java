package GroepswerkJava.DigitalPlanner.dto;

import GroepswerkJava.DigitalPlanner.model.Planning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeekPlanningWrapper {
    private List<PlanningDto> weekPlanning;

}
