/* (C)2023*/
package org.toolkit.spring.boot.cache.caffine.autoconfigure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CaffineAutoConfiguration {
	@Bean
	@ConditionalOnClass(com.github.benmanes.caffeine.cache.Cache.class)
	public Cache<String, Object> caffeineCache() {
		return Caffeine.newBuilder()
				.expireAfterWrite(60, TimeUnit.SECONDS)
				.initialCapacity(100)
				.maximumSize(1000)
				.build();
	}
}
