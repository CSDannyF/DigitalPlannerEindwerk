package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="planning")
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "workingDayId", referencedColumnName = "workingDayId")
    private WorkingDay workingDay;

    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanningDetail> planningDetails;

    @ManyToOne
    @JoinColumn(name = "planningStatusId")
    private PlanningStatus planningStatus;

    @Column(name = "plannedByStaff")
    private String plannedByStaff;

    @Column(name = "plannedDate")
    private LocalDate plannedDate;

    @Override
    public String toString() {
        return "Planning{" +
                "planningId=" + planningId +
                "planningDetails=" + planningDetails +

                ", planningStatus=" + planningStatus.getPlanningStatusName() +
                ", plannedByStaff='" + plannedByStaff + '\'' +
                ", plannedDate=" + plannedDate +
                '}';
    }
}
