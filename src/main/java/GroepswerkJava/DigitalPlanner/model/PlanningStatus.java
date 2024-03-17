package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "planningStatus", cascade = CascadeType.ALL)
    private List<Planning> planning;
}
