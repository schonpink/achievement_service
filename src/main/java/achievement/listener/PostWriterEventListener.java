package achievement.listener;

import achievement.dto.PostEventDto;
import achievement.handler.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostWriterEventListener extends AbstractListener<PostEventDto> {

    public PostWriterEventListener(ObjectMapper objectMapper,
                                   List<EventHandler<PostEventDto>> eventHandlers,
                                   @Value("${spring.achievements.post.writer.title}") String achievementTitle) {
        super(objectMapper, eventHandlers, achievementTitle);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostEventDto event = readValue(message.getBody(), PostEventDto.class);
        handleEvent(event);
    }
}