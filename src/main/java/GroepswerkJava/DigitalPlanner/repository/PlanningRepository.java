package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {
    Planning findByWorkingDay(WorkingDay workingDay);

    /*
    @Modifying
    @Query("UPDATE Planning\n" +
            "SET plannedByStaff=NULL, planned_date=NULL, planning_status_id=NULL, working_day_id=NULL\n" +
            "WHERE planning_id=0;")
    int updatePlanning()
     */
}
