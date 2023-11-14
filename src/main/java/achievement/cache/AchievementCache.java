package achievement.cache;

import achievement.model.Achievement;
import achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final Map<String, Achievement> achievementMap = new HashMap<>();
    private final AchievementRepository achievementRepository;

    @PostConstruct
    public void initCache() {
        List<Achievement> achievements = achievementRepository.findAll();
        for (Achievement achievement : achievements) {
            achievementMap.put(achievement.getTitle(), achievement);
        }
    }

    public Achievement get(String title) {
        return achievementMap.get(title);
    }
}