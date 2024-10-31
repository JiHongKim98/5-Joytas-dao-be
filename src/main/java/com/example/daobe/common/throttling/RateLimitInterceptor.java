package com.example.daobe.common.throttling;

import static com.example.daobe.common.throttling.RateLimitExceptionType.RATE_LIMIT_USER_ID_ERROR_MESSAGE;
import static com.example.daobe.common.throttling.RateLimitExceptionType.THROTTLING_EXCEPTION;

import com.example.daobe.common.throttling.annotation.RateLimited;
import com.example.daobe.common.utils.DaoStringUtils;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String DELIMITER = ":";
    private static final int CONSUME_TOKEN_COUNT = 1;
    private static final String HEADER_REMAIN = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After";

    private final LettuceBasedProxyManager<String> proxyManager;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        RateLimited rateLimited = extractRateLimitedAnnotation(handler);
        if (rateLimited == null) {
            return true;
        }

        Long userId = extractUserIdFromSecurityContext();
        if (userId == null) {
            throw new RateLimitException(RATE_LIMIT_USER_ID_ERROR_MESSAGE);
        }

        BucketProxy bucket = getBucketProxy(rateLimited, userId);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_TOKEN_COUNT);
        setRateLimitHeaders(response, probe);
        if (!probe.isConsumed()) {
            throw new RateLimitException(THROTTLING_EXCEPTION);
        }
        return true;
    }

    private Long extractUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    private RateLimited extractRateLimitedAnnotation(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.getMethodAnnotation(RateLimited.class);
        }
        return null;
    }

    private BucketProxy getBucketProxy(RateLimited rateLimited, Long userId) {
        BucketConfiguration config = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(rateLimited.capacity())
                        .refillGreedy(rateLimited.refillTokens(), Duration.ofSeconds(rateLimited.refillSeconds())))
                .build();
        return proxyManager.getProxy(generateBucketName(rateLimited.name(), userId), () -> config);
    }

    private String generateBucketName(String rateLimiterName, Long userId) {
        return DaoStringUtils.combineToString(rateLimiterName, DELIMITER, userId);
    }

    private void setRateLimitHeaders(HttpServletResponse response, ConsumptionProbe probe) {
        long remainingTokens = probe.getRemainingTokens();
        long waitForRefillMillis = TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill());

        response.setHeader(HEADER_REMAIN, String.valueOf(waitForRefillMillis));
        response.setHeader(HEADER_RETRY_AFTER, String.valueOf(remainingTokens));
    }
}
