package GroepswerkJava.DigitalPlanner.PlanningGenerator;

import GroepswerkJava.DigitalPlanner.model.Planning;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    private List<Planning> plannings;
    private List<String> logging;  // Todo misschien nog wegschrijven in csv file
}
