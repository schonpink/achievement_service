package achievement.cache;

import achievement.model.Achievement;
import achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementCacheTest {
    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache achievementCache;

    @BeforeEach
    public void setup() {
        Achievement achievement1 = Achievement.builder().title("Title 1").build();
        Achievement achievement2 = Achievement.builder().title("Title 2").build();

        List<Achievement> achievements = Arrays.asList(achievement1, achievement2);

        when(achievementRepository.findAll()).thenReturn(achievements);

        achievementCache.initCache();
    }

    @Test
    public void testGetAchievementByTitle() {
        Achievement achievement = achievementCache.get("Title 1");
        assertEquals("Title 1", achievement.getTitle());
    }

    @Test
    public void testGetNonExistentAchievement() {
        Achievement achievement = achievementCache.get("Nonexistent Title");
        assertNull(achievement);
    }
}