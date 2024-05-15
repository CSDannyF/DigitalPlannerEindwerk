package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Planner implements PlanningGenerator {

    private List<Staff> staff;
    private List<WorkingDay> workingDays;
    private List<Planning> plannings;

    @Override
    public Response generatePlanning(List<Staff> staff, List<WorkingDay> workingDays, List<Planning> plannings) {
        return null;
    }

}
