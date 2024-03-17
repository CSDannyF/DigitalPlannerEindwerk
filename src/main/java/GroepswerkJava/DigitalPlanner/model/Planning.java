package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="planning")
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningId;

    @OneToOne
    @JoinColumn(name = "workingDayId", referencedColumnName = "workingDayId")
    private WorkingDay workingDay;

    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL)
    private List<PlanningDetail> planningDetails;


    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL)
    private List<PlanningStatus> planningStatus;
}
