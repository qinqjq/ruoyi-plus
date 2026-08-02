package com.ruoyi.framework.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.ruoyi.framework.config.properties.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * redis配置
 *
 * @author Lion Li
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";
	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Autowired
	private RedisProperties redisProperties;

	@Autowired
	private RedissonProperties redissonProperties;

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redisson() throws IOException {
		String prefix = REDIS_PROTOCOL_PREFIX;
		if (redisProperties.isSsl()) {
			prefix = REDISS_PROTOCOL_PREFIX;
		}
		Config config = new Config();
		config.setThreads(redissonProperties.getThreads())
			.setNettyThreads(redissonProperties.getNettyThreads())
			.setCodec(JsonJacksonCodec.INSTANCE)
			.setTransportMode(redissonProperties.getTransportMode());

		RedissonProperties.SingleServerConfig singleServerConfig = redissonProperties.getSingleServerConfig();
		// 使用单机模式
		config.useSingleServer()
			.setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
			.setConnectTimeout(((Long) redisProperties.getTimeout().toMillis()).intValue())
			.setDatabase(redisProperties.getDatabase())
			.setPassword(StrUtil.isNotBlank(redisProperties.getPassword()) ? redisProperties.getPassword() : null)
			.setTimeout(singleServerConfig.getTimeout())
			.setRetryAttempts(singleServerConfig.getRetryAttempts())
			.setRetryInterval(singleServerConfig.getRetryInterval())
			.setSubscriptionsPerConnection(singleServerConfig.getSubscriptionsPerConnection())
			.setClientName(singleServerConfig.getClientName())
			.setIdleConnectionTimeout(singleServerConfig.getIdleConnectionTimeout())
			.setSubscriptionConnectionMinimumIdleSize(singleServerConfig.getSubscriptionConnectionMinimumIdleSize())
			.setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
			.setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
			.setConnectionPoolSize(singleServerConfig.getConnectionPoolSize())
			.setDnsMonitoringInterval(singleServerConfig.getDnsMonitoringInterval());

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 解决jackson2无法反序列化LocalDateTime的问题
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JacksonConfig.init(om);
        config.setCodec(new JsonJacksonCodec(om));

		return Redisson.create(config);
	}

	/**
	 * 整合spring-cache
	 */
	@Bean
	public CacheManager cacheManager(RedissonClient redissonClient) {
		Map<String, CacheConfig> config = new HashMap<>();
		config.put("redissonCacheMap", new CacheConfig(30*60*1000, 10*60*1000));
		return new RedissonSpringCacheManager(redissonClient, config, JsonJacksonCodec.INSTANCE);
	}

}
