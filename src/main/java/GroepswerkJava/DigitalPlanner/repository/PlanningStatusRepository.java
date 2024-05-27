package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.PlanningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanningStatusRepository extends JpaRepository<PlanningStatus, Long>
{
    PlanningStatus findByPlanningStatusName(String planningStatusName);

    PlanningStatus findByPlanningStatusId(long planningStatusId);

}
