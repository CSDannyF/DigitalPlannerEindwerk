package GroepswerkJava.DigitalPlanner.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planningDetail")
public class PlanningDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningDetailId;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "planningId", nullable = false)
    private Planning planning;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "staffId")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "availabilityId")
    private Availability availability;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "workMinutesPerDay")
    private int workMinutesPerDay;

    @Override
    public String toString() {
        return "PlanningDetail{" +
                "planningDetailId=" + planningDetailId +
                ", staff=" + staff +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
