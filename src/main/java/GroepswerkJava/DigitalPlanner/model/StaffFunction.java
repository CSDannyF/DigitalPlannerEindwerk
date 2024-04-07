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
@Table(name = "staffFunction")
public class StaffFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long functionId;

    @Column(name = "functionName", unique = true)
    private String functionName;

    @Column(name = "functionDescription")
    private String functionDescription;

    @OneToMany(mappedBy = "staffFunction",cascade = CascadeType.ALL)
    private List<Staff> staff;
}
