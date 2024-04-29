package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE working_day", nativeQuery = true)
    void truncateTable();

    WorkingDay findByDate(LocalDate date);

    WorkingDay findByWorkingDayId(int id);

}
