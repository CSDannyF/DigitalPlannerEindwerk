package GroepswerkJava.DigitalPlanner;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.Planner;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.Response;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
@SpringBootApplication
public class DigitalPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalPlannerApplication.class, args);
		Planner planner = new Planner();
		List<Staff> staffList = planner.createStaff();
		List<WorkingDay> workingDayList = planner.createWorkingDays();

		Response response = planner.generatePlanning(staffList, workingDayList);
	}
}