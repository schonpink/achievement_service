package achievement.handler.post;

import achievement.cache.AchievementCache;
import achievement.handler.AbstractAchievementHandler;
import achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractPostEventHandler <T> extends AbstractAchievementHandler<T> {
    public AbstractPostEventHandler(AchievementCache achievementCache, AchievementService achievementService) {
        super(achievementCache, achievementService);
    }
}