package achievement.handler.post;

import achievement.cache.AchievementCache;
import achievement.dto.PostEventDto;
import achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WriterHandler extends AbstractPostEventHandler<PostEventDto> {
    @Value("${spring.achievements.post.writer.title}")
    private String achievementTitle;

    public WriterHandler(AchievementCache achievementCache, AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handle(PostEventDto event) {
        handleAchievement(achievementTitle, event.getAuthorId());
    }

    @Override
    public String getAchievementTitle() {
        return achievementTitle;
    }
}