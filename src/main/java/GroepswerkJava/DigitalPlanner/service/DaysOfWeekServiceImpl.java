package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class DaysOfWeekServiceImpl implements DaysOffWeekService, CommandLineRunner {

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

    //Vaste list met dagen van de week
    private static final List<String> WEEKS_DAYS = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    @Override
    public List<DaysOffWeekDto> getAll()
    {
        List<DaysOffWeek> days = daysOfWeekRepository.findAll();
        return days.stream()
                .map((daysOffWeek) -> mapToDaysOffWeekDto(daysOffWeek))
                .collect(Collectors.toList());
    }

    @Override
    public void save(DaysOffWeekDto daysOffWeek)
    {
        DaysOffWeek day = new DaysOffWeek();
        day.setDayName(daysOffWeek.getDayName());
        daysOfWeekRepository.save(day);
    }

    @Override
    public DaysOffWeekDto getById(long id) {
        Optional<DaysOffWeek> optional = daysOfWeekRepository.findById(id);
        DaysOffWeek daysOffWeek = null;
        if(optional.isPresent())
        {
            daysOffWeek = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return mapToDaysOffWeekDto(daysOffWeek);
    }

    @Override
    public DaysOffWeekDto getByName(String name)
    {
        return mapToDaysOffWeekDto(daysOfWeekRepository.findByDayName(name));
    }

    //protected gebruikt zodat ze enkel kunnen worder aangeroepen in de service klassen.

    protected DaysOffWeekDto mapToDaysOffWeekDto(DaysOffWeek day)
    {
        DaysOffWeekDto daysOffWeekDto = new DaysOffWeekDto();
        daysOffWeekDto.setDaysOffWeekId(day.getDaysOfWeekId());
        daysOffWeekDto.setDayName(day.getDayName());
        return daysOffWeekDto;
    }

    protected DaysOffWeek mapToDaysOffWeekModel(DaysOffWeekDto day)
    {
        DaysOffWeek daysOffWeek = new DaysOffWeek();
        daysOffWeek.setDaysOfWeekId(day.getDaysOffWeekId());
        daysOffWeek.setDayName(day.getDayName());
        return daysOffWeek;
    }

    /*
    Methode om de dagen van de week te initializeren bij de eerste opstart van de applicatie.
    Via de repository gaan we kijken als deze tabel al dan niet leeg is.
    Indien deze leeg is gaan we deze vullen met de list WEEK_DAYS, anders gaan we dit overslaan
    */
    private void initializeWeekdays()
    {
            for(String day : WEEKS_DAYS)
            {
                DaysOffWeek daysOffWeek = new DaysOffWeek();
                daysOffWeek.setDayName(day);
                daysOfWeekRepository.save(daysOffWeek);
            }
    }

    /*
    Methode van de interface CommandLineRunner.
    Deze Methode wordt uitgevoerd bij het opstarten van de website/app en gaat de methode initializeWeekdays() uitvoeren
     */
    @Override
    public void run(String... args) throws Exception
    {
        if(daysOfWeekRepository.count() == 0) {
            initializeWeekdays();
        }
    }
}
