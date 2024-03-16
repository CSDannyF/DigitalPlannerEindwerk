package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dailyException")
public class DailyException { //nog linken aan workingDay

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dailyExceptionId;

    @Column( name = "date",nullable = false, unique = true)
    private Date date;

    @Column(name = "isOpen" ,nullable = false)
    private boolean isOpen;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "endTime")
    private Time endTime;

    @OneToOne(mappedBy = "dailyException", cascade = CascadeType.ALL)
    private WorkingDay workingDay;
}
