package achievement.service;

import achievement.dto.AchievementEventDto;
import achievement.exception.EntityNotFoundException;
import achievement.model.Achievement;
import achievement.model.AchievementProgress;
import achievement.model.UserAchievement;
import achievement.publisher.AchievementEventPublisher;
import achievement.repository.AchievementProgressRepository;
import achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementEventPublisher achievementEventPublisher;

    @Transactional(readOnly = true)
    public boolean userHasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement progress with userId: "
                        + userId + " and achievementId: " + achievementId + " not found"));
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class)
    public void incrementProgress(AchievementProgress achievementProgress) {
        achievementProgress.increment();
        achievementProgressRepository.save(achievementProgress);
    }

    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
        publishAchievementEvent(userAchievement);
    }

    private void publishAchievementEvent(UserAchievement achievement) {
        AchievementEventDto event = AchievementEventDto.builder()
                .achievementId(achievement.getId())
                .authorId(achievement.getUserId())
                .title(achievement.getAchievement().getTitle())
                .build();

        achievementEventPublisher.publish(event);
    }
}