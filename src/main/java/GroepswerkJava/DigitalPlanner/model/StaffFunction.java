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
    private long staffFunctionId;

    @Column(name = "staffFunctionName", unique = true)
    private String staffFunctionName;

    @Column(name = "staffFunctionDescription")
    private String staffFunctionDescription;

    @OneToMany(mappedBy = "staffFunction",cascade = CascadeType.ALL)
    private List<Staff> staff;
}
