package com.chat.services;

import com.chat.data.entities.Application;
import com.chat.data.repositories.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initializes mocks and injects them into the service
    }

    @Test
    public void testCreateApplication() {
        Application app = new Application();
        app.setName("TestApp");
        app.setToken(new byte[16]);

        // Mock the behavior of applicationRepository.save
        when(applicationRepository.save(app)).thenReturn(app);

        // Call the service method
        Application createdApp = applicationService.createApplication(app);

        // Verify and assert results
        assertThat(createdApp).isNotNull();
        assertThat(createdApp.getName()).isEqualTo("TestApp");
    }

    @Test
    public void testGetApplicationByToken() {
        byte[] token = new byte[16];
        Application app = new Application();
        app.setName("TestApp");
        app.setToken(token);

        // Mock the behavior of applicationRepository.findByToken
        when(applicationRepository.findByToken(token)).thenReturn(app);

        // Call the service method
        Application foundApp = applicationService.getApplicationByToken(token);

        // Verify and assert results
        assertThat(foundApp).isNotNull();
        assertThat(foundApp.getName()).isEqualTo("TestApp");
    }
}
