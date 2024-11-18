package com.chat.services;

import com.chat.api.controllers.resources.ApplicationRequest;
import com.chat.api.controllers.resources.ApplicationResponse;
import com.chat.data.entities.Application;
import com.chat.data.repositories.ApplicationRepository;
import com.chat.data.repositories.ChatRepository;
import com.chat.mappers.ApplicationMapper;
import com.chat.utils.TokenConverter;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final RedisCacheService redisCacheService;
    private final ChatRepository chatRepository;


    public List<ApplicationResponse> getAllApplications(int page, int limit) {
        return applicationMapper.toResponse(applicationRepository.findAllPaged((page - 1) * limit, limit));
    }

    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        Application application = applicationMapper.toEntity(applicationRequest);
        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    public ApplicationResponse getApplicationByToken(String token) {
        return applicationMapper.toResponse(getByToken(token));
    }

    public ApplicationResponse update(String token, ApplicationRequest request) {
        Application application = getByToken(token);
        application.setName(request.getName());
        applicationRepository.save(application);
        return applicationMapper.toResponse(application);
    }
    public void deleteApplication(String token) {
        Application application = getByToken(token);
        applicationRepository.delete(application);
    }
    public Long getApplicationIdByToken(String token){
        // Check Redis cache
        String cachedApplicationId = redisCacheService.getCachedApplicationId(token);
        if (cachedApplicationId != null) {
           return Long.parseLong(cachedApplicationId);
        } else {
            Application application = applicationRepository.findByToken(TokenConverter.toBytes(token))
                    .orElseThrow(() -> new RuntimeException("Application not found"));
            redisCacheService.cacheApplication(token, String.valueOf(application.getId()));
            return application.getId();
        }
    }

    public Application getByToken(String token){
        // Check Redis cache
        String cachedApplicationId = redisCacheService.getCachedApplicationId(token);
        Application application;
        if (cachedApplicationId != null) {
            application = applicationRepository.findById(Long.parseLong(cachedApplicationId))
                    .orElseThrow(() -> new RuntimeException("Application not found"));
        } else {
            application = applicationRepository.findByToken(TokenConverter.toBytes(token))
                    .orElseThrow(() -> new RuntimeException("Application not found"));
            redisCacheService.cacheApplication(token, String.valueOf(application.getId()));
        }
        return application;
    }

    public Integer getNewChatNumber(Long applicationId, String applicationToken) {
        String redisKey = applicationToken + "_chats_count";
        String lockKey = redisKey + "_lock";

        // Use Redis atomic increment if the key exists
        if (redisCacheService.exists(redisKey)) {
            return redisCacheService.increment(redisKey);
        }

        // Fallback to lock mechanism
        return generateChatNumberWithLock(applicationId, redisKey, lockKey);
    }
    private Integer generateChatNumberWithLock(Long applicationId, String redisKey, String lockKey) {
        RLock lock = null;
        try {
            lock = redisCacheService.acquireLock(lockKey, 2, 5); // 2 seconds wait, 5 seconds lease time

            // Double-check if the key exists inside the lock
            int chatsInQueueMax = redisCacheService.get(redisKey) != null ? Integer.parseInt(redisCacheService.get(redisKey)) : 0;

            // Get the max chat number from the database
            Integer chatsDbMax = chatRepository.findMaxNumberByApplicationId(applicationId).orElse(0);

            // Calculate the new chat number
            int newChatNumber = Math.max(chatsInQueueMax, chatsDbMax) + 1;

            // Save the new number in Redis with an expiry
            redisCacheService.set(redisKey, String.valueOf(newChatNumber), Duration.ofHours(1));

            return newChatNumber;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted while waiting for lock.", e);
        } finally {
            redisCacheService.releaseLock(lock);
        }
    }

}
