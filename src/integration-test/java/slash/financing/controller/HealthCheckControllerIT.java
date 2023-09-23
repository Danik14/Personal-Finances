package slash.financing.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HealthCheckControllerIT {

    @InjectMocks
    private HealthCheckController healthCheckController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHealthCheck() {
        ResponseEntity<String> responseEntity = healthCheckController.healchCheck();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Hello World", responseEntity.getBody());
    }
}
