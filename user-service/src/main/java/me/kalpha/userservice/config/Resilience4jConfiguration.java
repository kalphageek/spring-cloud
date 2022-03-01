package me.kalpha.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import java.time.Duration;


public class Resilience4jConfiguration {
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(4) //open결정시간. default 50
                .waitDurationInOpenState(Duration.ofMillis(1000)) //open유지시간. default 60초
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) //close하면서 기록할 때 window 유형.카운트기반 또는 시간기반
                .slidingWindowSize(2) //window크기. default 100
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)) //TimeLimiter는 future supplier의 time limit을 결정하는 API. default 1초
                .build();

        return null;
    }
}
