package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanningRepository extends JpaRepository<Planning, Long> {
    Planning findByWorkingDay(WorkingDay workingDay);

}
