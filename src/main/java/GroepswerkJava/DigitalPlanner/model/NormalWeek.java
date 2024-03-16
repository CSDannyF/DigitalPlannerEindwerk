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
@Table(name = "normalWeek")
public class NormalWeek { //nog linken aan workingDay

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long NormalWeekId;

    /*deze moet geen date zijn maar een dag van de week, maar hoe gaan we dit declareren? effe bespreken
    String dayOfTheWeek? zie persoonlijke notities voor meer info. zou in orde moeten zijn */
    @Column(nullable = false, unique = true, name = "date")
    private int dayNumber;

    @Column(name = "dayName")
    private String dayName;

    @Column(nullable = false, name = "isOpen")
    private boolean isOpen;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "endTime")
    private Time endTime;

    @OneToMany(mappedBy = "normalWeek",cascade = CascadeType.ALL)
    private List<WorkingDay> workingDays;
}
