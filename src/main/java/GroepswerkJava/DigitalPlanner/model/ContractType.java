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
@Table(name = "contractType")
public class ContractType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contractTypeId;

    @Column(name = "contractTypeName", nullable = false)
    private String contractTypeName;

    @Column(name = "hoursPerWeek", nullable = false)
    private int hoursPerWeek;

    @OneToMany(mappedBy = "contractType",cascade = CascadeType.ALL)
    private List<Staff> staffs;
}
