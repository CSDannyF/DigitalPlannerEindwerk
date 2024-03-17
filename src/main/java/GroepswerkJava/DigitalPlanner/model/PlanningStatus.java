package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="planningStatus")
public class PlanningStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningStatusId;

    @Column(nullable = false, unique = true, name = "planningStatusName")
    private String planningStatusName;

    @ManyToOne
    @JoinColumn(name = "planningStatus", nullable=false)
    private Planning planning;
}
