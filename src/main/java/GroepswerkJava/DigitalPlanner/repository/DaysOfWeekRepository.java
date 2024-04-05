package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaysOfWeekRepository extends JpaRepository<DaysOffWeek, Long>
{
    DaysOffWeek findByDayName(String name);
}
