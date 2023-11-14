package achievement.handler.skill;

import achievement.cache.AchievementCache;
import achievement.handler.AbstractAchievementHandler;
import achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractSkillEventHandler<T> extends AbstractAchievementHandler<T> {
    public AbstractSkillEventHandler(AchievementCache achievementCache, AchievementService achievementService) {
        super(achievementCache, achievementService);
    }
}