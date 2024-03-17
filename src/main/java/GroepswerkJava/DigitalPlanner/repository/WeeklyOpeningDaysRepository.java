package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WeeklyOpeningDaysRepository extends JpaRepository<WeeklyOpeningDay, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE weekly_opening_days", nativeQuery = true)
    void truncateTable();
}
