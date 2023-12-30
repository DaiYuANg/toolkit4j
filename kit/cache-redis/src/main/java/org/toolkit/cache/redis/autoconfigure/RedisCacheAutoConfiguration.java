/* (C)2023*/
package org.toolkit.cache.redis.autoconfigure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisCacheAutoConfiguration {

	//	private ObjectMapper objectMapper;

	//	@Bean
	//	@ConditionalOnBean(name = "localLettuceConnectionFactory")
	//	public RedisTemplate<String, String> localRedisTemplate(LettuceConnectionFactory localLettuceConnectionFactory) {
	//		val valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
	//		return new RedisTemplate<>() {
	//			{
	//				setConnectionFactory(localLettuceConnectionFactory);
	//				setKeySerializer(new StringRedisSerializer());
	//				setValueSerializer(valueSerializer);
	//				setEnableTransactionSupport(true);
	//				setHashValueSerializer(valueSerializer);
	//				afterPropertiesSet();
	//			}
	//		};
	//	}
}
