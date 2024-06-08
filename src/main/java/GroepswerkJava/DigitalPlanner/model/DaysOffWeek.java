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
public class DaysOffWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long daysOfWeekId;

    @Column(name = "dayName")
    private String dayName;

    @ManyToMany(mappedBy = "daysOffWeek")
    private Set<Staff> staff;

    @Override
    public String toString() {
        return "DaysOffWeek{" +
                "daysOfWeekId=" + daysOfWeekId +
                ", dayName='" + dayName + '\'' +
                ", staff=" + staff +
                '}';
    }
}
