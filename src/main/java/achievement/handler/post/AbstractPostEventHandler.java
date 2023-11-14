package achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class AbstractPostEventHandler <T> extends AbstractAchievementHandler<T> {
    public AbstractPostEventHandler(AchievementCache achievementCache, AchievementService achievementService) {
        super(achievementCache, achievementService);
    }
}
