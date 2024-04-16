package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planningDetail")
public class PlanningDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningDetailId;

    @ManyToOne
    @JoinColumn(name = "planningId", nullable = false)
    private Planning planning;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "availabilityId")
    private Availability availability;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "endTime")
    private Time endTime;

    @Column(name = "workMinutesPerDay")
    private int workMinutesPerDay;
}
