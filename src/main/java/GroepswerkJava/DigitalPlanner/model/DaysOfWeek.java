package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daysOfWeek")
public class DaysOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long daysOfWeekId;

    @Column(name = "dayName")
    private String dayName;

    @ManyToMany(mappedBy = "daysOfWeek")
    private Set<Staff> staff;
}
