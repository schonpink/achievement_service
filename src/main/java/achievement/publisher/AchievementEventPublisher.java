package achievement.publisher;

import achievement.dto.AchievementEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AchievementEventPublisher extends AbstractPublisher<AchievementEventDto> {

    public AchievementEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                     ObjectMapper mapper,
                                     @Value("${spring.data.redis.channels.achievement}") String channel) {
        super(redisTemplate, mapper, channel);
    }
}