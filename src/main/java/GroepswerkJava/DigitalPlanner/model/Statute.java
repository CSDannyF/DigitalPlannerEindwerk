package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statute")
public class Statute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statuteId;

    @Enumerated(EnumType.STRING)
    private StatuteName statuteName;

    public long getStatuteId() {
        return statuteId;
    }

    public StatuteName getStatuteName() {
        return statuteName;
    }

    public void setStatuteName(StatuteName statuteName) {
        this.statuteName = statuteName;
    }
}
