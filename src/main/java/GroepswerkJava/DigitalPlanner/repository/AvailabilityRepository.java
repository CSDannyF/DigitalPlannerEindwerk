package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.Availability;
import GroepswerkJava.DigitalPlanner.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Availability findByStaffAndDate(Staff staff, LocalDate localDate);
}
