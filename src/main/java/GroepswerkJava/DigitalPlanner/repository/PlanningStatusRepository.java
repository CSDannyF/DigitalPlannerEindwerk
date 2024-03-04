package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.PlanningStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanningStatusRepository extends JpaRepository<PlanningStatus, Long> {
}
