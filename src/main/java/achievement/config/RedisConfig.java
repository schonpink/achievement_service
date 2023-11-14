package achievement.config;

import achievement.listener.PostWriterEventListener;
import achievement.listener.SkillEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.skill}")
    private String skillChannel;
    @Value("${spring.data.redis.channels.post}")
    private String postChannel;
    @Value("${spring.data.redis.channels.achievement}")
    private String achievementChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        log.info("Created new redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean(name = "skillAdapter")
    public MessageListenerAdapter skillAdapter(SkillEventListener skillEventListener) {
        return new MessageListenerAdapter(skillEventListener);
    }

    @Bean(name = "postAdapter")
    public MessageListenerAdapter postAdapter(PostWriterEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public ChannelTopic skillTopic() {
        return new ChannelTopic(skillChannel);
    }

    @Bean
    public ChannelTopic postTopic() {
        return new ChannelTopic(postChannel);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(@Qualifier("skillAdapter") MessageListenerAdapter skillAdapter,
                                                        @Qualifier("postAdapter") MessageListenerAdapter postAdapter,
                                                        @Qualifier("achievementAdapter") MessageListenerAdapter achievementAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(skillAdapter, skillTopic());
        container.addMessageListener(postAdapter, postTopic());
        container.addMessageListener(achievementAdapter, achievementTopic());
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}