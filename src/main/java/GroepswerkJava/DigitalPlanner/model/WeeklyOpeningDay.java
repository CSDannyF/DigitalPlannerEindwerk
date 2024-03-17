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
@Table(name="weekly_opening_days")
public class WeeklyOpeningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long DayId;

    @Column(name = "day")
    private String day;

    @Column(name = "open")
    private boolean open;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "minimal_occupation")
    private int minOccupation;

    public WeeklyOpeningDay(String day){
        this.day=day;
    }

    @Override
    public String toString() {
        return "WeeklyOpeningDays{" +
                "DayId=" + DayId +
                ", day='" + day + '\'' +
                ", open=" + open +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", minOccupation=" + minOccupation +
                '}';
    }
}
