package com.chat.data.services;

import com.chat.data.entities.Application;
import com.chat.data.repositories.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;


    public Application createApplication(Application application) {
        // Additional logic for generating a token or checking uniqueness can be added here
        return applicationRepository.save(application);
    }

    public Application getApplicationByToken(byte[] token) {
        return applicationRepository.findByToken(token);
    }


}
