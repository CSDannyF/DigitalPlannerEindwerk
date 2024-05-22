package GroepswerkJava.DigitalPlanner;

import GroepswerkJava.DigitalPlanner.dto.LogDto;
import GroepswerkJava.DigitalPlanner.model.Log;
import GroepswerkJava.DigitalPlanner.repository.LogRepository;
import GroepswerkJava.DigitalPlanner.service.LogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Add JUnit 5 support
@ExtendWith(MockitoExtension.class)
public class LogServiceImplTest {

    // Create mock object
    @Mock
    private LogRepository logRepository;

    // Inject mocked repo
    @InjectMocks
    private LogServiceImpl logService;

    private Log log;
    private LogDto logDto;

    // Initialize test data before each test
    @BeforeEach
    void setUp() {
        log = new Log();
        log.setLogId(1L);
        log.setAction("Test action");
        log.setPlannedDate(LocalDate.now());

        // Simulate mapping from Log to LogDto
        logDto = new LogDto();
        logDto.setLogId(1L);
        logDto.setAction("Test action");
        logDto.setPlannedDate(LocalDate.now());
    }

    // Check if logRepository.save() gets called exactly once when running saveLog()
    @Test
    void test_SaveLog() {
        when(logRepository.save(any(Log.class))).thenReturn(log);

        logService.saveLog(logDto);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    // Check if correct LogDto gets returned
    @Test
    void test_FindById() {
        when(logRepository.findById(1L)).thenReturn(Optional.of(log));

        LogDto found = logService.findById(1L);

        assertNotNull(found);
        assertEquals(logDto.getLogId(), found.getLogId());
        assertEquals(logDto.getAction(), found.getAction());
        assertEquals(logDto.getPlannedDate(), found.getPlannedDate());
    }

    // Check if expected list gets returned
    @Test
    void test_FindAllLogs() {
        List<Log> logs = Arrays.asList(log);
        when(logRepository.findAll()).thenReturn(logs);

        List<LogDto> logDtos = logService.findAllLogs();

        assertNotNull(logDtos);
        assertEquals(1, logDtos.size());
    }

    // Check if Log gets updated & saved correctly
    @Test
    void test_UpdateLog() {
        when(logRepository.existsById(1L)).thenReturn(true);
        when(logRepository.save(any(Log.class))).thenReturn(log);

        LogDto updated = logService.updateLog(1L, logDto);

        assertNotNull(updated);
        assertEquals(logDto.getLogId(), updated.getLogId());
        assertEquals(logDto.getAction(), updated.getAction());
        assertEquals(logDto.getPlannedDate(), updated.getPlannedDate());
    }

    // Check if correct LogId gets deleted
    @Test
    void test_DeleteLog() {
        when(logRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> logService.deleteLog(1L));

        verify(logRepository, times(1)).deleteById(1L);
    }
}