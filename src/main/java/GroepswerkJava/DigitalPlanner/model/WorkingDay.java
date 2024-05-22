package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="workingDay")
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workingDayId;

    @OneToOne(mappedBy = "workingDay", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Planning planning;

    @Column(name = "date",nullable = false, unique = true)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(name = "weekNumber", nullable = false)
    private int weekNumber;

    @Column(name = "isOpen", nullable = false)
    private boolean isOpen;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "minimalOccupation")
    private int minimalOccupation;

    @Column(name = "endTime")
    private LocalTime endTime;

    public WorkingDay(LocalDate date){
        this.date = date;
    }

    public WorkingDay(LocalDate startDate, boolean open, LocalTime startTime, LocalTime endTime, int minOccupation) {
        this.date = startDate;
        this.isOpen = open;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minimalOccupation = minOccupation;
    }

    @Override
    public String toString() {
        return "WorkingDay{" +
                "workingDayId=" + workingDayId +
                ", planning=" + planning +
                ", date=" + date +
                ", isOpen=" + isOpen +
                ", startTime=" + startTime +
                ", minimalOccupation=" + minimalOccupation +
                ", endTime=" + endTime +
                '}';
    }
}
