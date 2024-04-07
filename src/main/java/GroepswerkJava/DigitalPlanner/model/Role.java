package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @ManyToMany(mappedBy = "roles")
    private Set<UserLogin> userLogins;

    @Column(name = "roleName")
    private String roleName;

    @Override
    public String toString() {
        return roleName;
    }
}
