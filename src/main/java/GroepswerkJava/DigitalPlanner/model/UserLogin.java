package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="userLogin")
@Table(name = "userLogin")
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userLoginId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "changePassword")
    private Boolean changePassword;

    @Lob
    @Column (name = "userImageData", columnDefinition = "LONGBLOB")
    private byte[] userImageData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffId")
    private Staff staff;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "userRoles",
            joinColumns = {@JoinColumn(name = "userLoginId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        String hasRoles = "";
        for (Role role : this.getRoles()) {
            hasRoles += role.getRoleName() + ", ";
        }
        return "UserLogin{" +
                "userLoginId=" + userLoginId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", staff=" + staff +
                ", roles=" + hasRoles +
                '}';
    }
}
