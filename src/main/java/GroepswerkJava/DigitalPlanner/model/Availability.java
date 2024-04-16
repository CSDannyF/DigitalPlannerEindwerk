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
@Table(name="availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long availabilityId;

    @Column(name = "availabilityType")
    private String availabilityType;

    @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL)
    private List<PlanningDetail> planningDetails;
}
