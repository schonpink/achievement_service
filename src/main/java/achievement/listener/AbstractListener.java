package achievement.listener;

import achievement.exception.MessageReadException;
import achievement.handler.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;
    private final String title;

    protected T readValue(byte[] json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            log.error("Failed to read message", e);
            throw new MessageReadException(e);
        }
    }

    protected void handleEvent(T event) {
        handlers.stream()
                .filter(handler -> handler.getAchievementTitle().equalsIgnoreCase(title))
                .forEach(handler -> handler.handle(event));
    }
}