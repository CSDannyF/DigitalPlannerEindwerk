package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "planningStatusId")
    private PlanningStatus planningStatus;

    @Column(name = "plannedByStaff")
    private String plannedByStaff;

    @Column(name = "plannedDate")
    private LocalDate plannedDate;
}
