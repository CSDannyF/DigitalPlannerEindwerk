package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class DaysOfWeekServiceImpl implements DaysOffWeekService {

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

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
}
