package GroepswerkJava.DigitalPlanner;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.LogDetailMapper;
import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.model.LogDetail;
import GroepswerkJava.DigitalPlanner.model.LogType;
import GroepswerkJava.DigitalPlanner.repository.LogDetailRepository;
import GroepswerkJava.DigitalPlanner.service.LogDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Add JUnit 5 support
@ExtendWith(MockitoExtension.class)
public class LogDetailServiceImplTest {

    // Create mock object
    @Mock
    private LogDetailRepository logDetailRepository;

    // Inject mocked repo
    @InjectMocks
    private LogDetailServiceImpl logDetailService;

    private LogDetailMapper logDetailMapper = LogDetailMapper.INSTANCE;
    private LogDetail logDetail;
    private LogDetailDto logDetailDto;

    // Initialize test data before each test
    @BeforeEach
    void setUp() {
        logDetail = new LogDetail();
        logDetail.setLogDetailId(1L);
        logDetail.setLogType(LogType.INFO);
        logDetail.setMessage("Test message");

        logDetailDto = logDetailMapper.logDetailToLogDetailDto(logDetail);
    }

    // Check if logDetailRepository.save() gets called exactly once when running saveLogDetail()
    @Test
    void test_SaveLogDetail() {
        when(logDetailRepository.save(any(LogDetail.class))).thenReturn(logDetail);

        logDetailService.saveLogDetail(logDetailDto);

        verify(logDetailRepository, times(1)).save(any(LogDetail.class));
    }

    // Check if correct LogDetailDto gets returned
    @Test
    void test_FindById() {
        when(logDetailRepository.findById(1L)).thenReturn(Optional.of(logDetail));

        LogDetailDto found = logDetailService.findById(1L);

        assertNotNull(found);
        assertEquals(logDetailDto.getLogDetailId(), found.getLogDetailId());
        assertEquals(logDetailDto.getLogType(), found.getLogType());
        assertEquals(logDetailDto.getMessage(), found.getMessage());
    }

    // Check if expected list gets returned
    @Test
    void test_FindAllLogDetails() {
        List<LogDetail> logDetails = Arrays.asList(logDetail);
        when(logDetailRepository.findAll()).thenReturn(logDetails);

        List<LogDetailDto> logDetailDtos = logDetailService.findAllLogDetails();

        assertNotNull(logDetailDtos);
        assertEquals(1, logDetailDtos.size());
    }

    // Check if LogDetail gets updated & saved correctly
    @Test
    void test_UpdateLogDetail() {
        when(logDetailRepository.existsById(1L)).thenReturn(true);
        when(logDetailRepository.save(any(LogDetail.class))).thenReturn(logDetail);

        LogDetailDto updated = logDetailService.updateLogDetail(1L, logDetailDto);

        assertNotNull(updated);
        assertEquals(logDetailDto.getLogDetailId(), updated.getLogDetailId());
        assertEquals(logDetailDto.getLogType(), updated.getLogType());
        assertEquals(logDetailDto.getMessage(), updated.getMessage());
    }

    // Check if correct LogDetailId gets deleted
    @Test
    void test_DeleteLogDetail() {
        when(logDetailRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = logDetailService.deleteLogDetail(1L);

        assertTrue(isDeleted);
        verify(logDetailRepository, times(1)).deleteById(1L);
    }
}