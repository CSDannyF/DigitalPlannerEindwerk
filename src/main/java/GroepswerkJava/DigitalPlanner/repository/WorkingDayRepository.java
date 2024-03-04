package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
}
