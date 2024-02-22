package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "days_of_week")
public class DaysOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long daysOfWeekId;

    @Column(name = "description")
    private String description;

    public long getDaysOfWeekId() {
        return daysOfWeekId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
