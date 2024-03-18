package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="workingDay")
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workingDayId;

    @OneToOne(mappedBy = "workingDay", cascade = CascadeType.ALL)
    private Planning planning;

    @Column(name = "date",nullable = false, unique = true)
    private Date date;

    @Column(name = "isOpen", nullable = false)
    private boolean isOpen;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "minimalOccupation")
    private int minimalOccupation;

    @Column(name = "endTime")
    private Time endTime;
}
