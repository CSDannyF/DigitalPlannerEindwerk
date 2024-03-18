package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name="weeklyOpeningDays")
public class WeeklyOpeningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long DayId;

    @Column(name = "day")
    private String day;

    @Column(name = "open")
    private boolean isOpen;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "minimalOccupation")
    private int minOccupation;

    public WeeklyOpeningDay(String day){
        this.day=day;
    }

    @Override
    public String toString() {
        return "WeeklyOpeningDays{" +
                "DayId=" + DayId +
                ", day='" + day + '\'' +
                ", open=" + isOpen +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", minOccupation=" + minOccupation +
                '}';
    }
}
