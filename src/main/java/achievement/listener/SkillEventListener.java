package achievement.listener;

import achievement.dto.SkillAcquiredEventDto;
import achievement.handler.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillEventListener extends AbstractListener<SkillAcquiredEventDto> {

    public SkillEventListener(
            ObjectMapper objectMapper,
            List<EventHandler<SkillAcquiredEventDto>> handlers,
            @Value("${spring.achievements.skill.title}") String achievementTitle) {
        super(objectMapper, handlers, achievementTitle);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SkillAcquiredEventDto event = readValue(message.getBody(), SkillAcquiredEventDto.class);
        handleEvent(event);
    }
}