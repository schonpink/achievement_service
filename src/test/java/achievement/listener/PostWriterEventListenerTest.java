package achievement.listener;

import achievement.dto.PostEventDto;
import achievement.handler.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostWriterEventListenerTest {
    @InjectMocks
    private PostWriterEventListener postEventListener;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<EventHandler<PostEventDto>> eventHandlers;

    private PostEventDto postEventDto;
    private Message message;


    @BeforeEach
    void setUp() throws IOException {
        postEventDto = mock(PostEventDto.class);

        message = mock(Message.class);
        byte[] body = new byte[0];

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(message.getBody(), PostEventDto.class))
                .thenReturn(postEventDto);
    }

    @Test
    void onMessage_shouldInvokeObjectMapperReadValueMethod() throws IOException {
        postEventListener.onMessage(message, new byte[0]);
        verify(objectMapper).readValue(message.getBody(), PostEventDto.class);
    }

    @Test
    void onMessage_shouldInvokeHandleEventMethod() {
        postEventListener.onMessage(message, new byte[0]);
        eventHandlers.forEach(handler -> verify(handler).handle(postEventDto));
    }
}