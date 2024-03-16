package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "statute")
public class Statute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statuteId;

    @Column(name = "statuteName")
    private String statuteName;

    @OneToMany(mappedBy = "statute",cascade = CascadeType.ALL)
    private List<Staff> staffs;
}