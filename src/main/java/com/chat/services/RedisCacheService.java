package com.chat.services;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
    public class RedisCacheService {

    private static final long CACHE_TTL = 1; // Time-to-Live in hours

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;


    public void cacheApplication(String token, String applicationId) {
        redisTemplate.opsForValue().set(token, applicationId, CACHE_TTL, TimeUnit.HOURS);
    }

    public String getCachedApplicationId(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * Check if a key exists in Redis.
     *
     * @param key the Redis key
     * @return true if the key exists, false otherwise
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Increment a Redis key atomically.
     *
     * @param key the Redis key
     * @return the incremented value
     */
    public Integer increment(String key) {
        return redisTemplate.opsForValue().increment(key).intValue();
    }

    /**
     * Get the value of a Redis key.
     *
     * @param key the Redis key
     * @return the value or null if the key does not exist
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Set a value in Redis with an optional expiry time.
     *
     * @param key    the Redis key
     * @param value  the value to set
     * @param expiry the expiration duration (can be null)
     */
    public void set(String key, String value, Duration expiry) {
        if (expiry != null) {
            redisTemplate.opsForValue().set(key, value, expiry);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * Acquire a distributed lock.
     *
     * @param lockKey    the Redis lock key
     * @param waitTime   the maximum time to wait for the lock
     * @param leaseTime  the time to hold the lock after granting
     * @return the lock object (must be released by the caller)
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public RLock acquireLock(String lockKey, long waitTime, long leaseTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
            return lock;
        } else {
            throw new IllegalStateException("Unable to acquire lock for key: " + lockKey);
        }
    }

    /**
     * Release a distributed lock.
     *
     * @param lock the lock to release
     */
    public void releaseLock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
