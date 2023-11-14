package achievement.repository;

import achievement.model.AchievementProgress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementProgressRepository extends CrudRepository<AchievementProgress, Long> {

    @Query(value = """
                    SELECT ap
                    FROM AchievementProgress ap
                    WHERE ap.userId = :userId AND ap.achievement.id = :achievementId
            """)
    Optional<AchievementProgress> findByUserIdAndAchievementId(@Param("userId") long userId, @Param("achievementId") long achievementId);

    @Query(nativeQuery = true, value = """
                    INSERT INTO user_achievement_progress (user_id, achievement_id, current_points)
                    VALUES (:userId, :achievementId, 0)
                    ON CONFLICT DO NOTHING
            """)
    @Modifying
    void createProgressIfNecessary(@Param("userId") long userId, @Param("achievementId") long achievementId);

    List<AchievementProgress> findByUserId(long userId);
}