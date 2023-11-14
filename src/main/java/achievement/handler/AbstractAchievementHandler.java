package achievement.handler;

import achievement.cache.AchievementCache;
import achievement.model.Achievement;
import achievement.model.AchievementProgress;
import achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractAchievementHandler <T> implements EventHandler<T> {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Async("taskExecutor")
    protected void handleAchievement(String achievementTitle, long userId) {
        Achievement achievement = achievementCache.get(achievementTitle);
        long achievementId = achievement.getId();

        if (achievementService.userHasAchievement(userId, achievementId)) {
            log.info("User: {} already has achievement {}", userId, achievement.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getProgress(userId, achievementId);
        achievementService.incrementProgress(progress);

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement);
            log.info("User: {} has achieved achievement: {}", userId, achievement.getTitle());
        }
    }
}
