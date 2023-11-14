package achievement.repository;

import achievement.model.UserAchievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {

    @Query(value = """
                    SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END
                    FROM UserAchievement ua
                    WHERE ua.userId = :userId AND ua.achievement.id = :achievementId
            """)
    boolean existsByUserIdAndAchievementId(@Param("userId") long userId, @Param("achievementId") long achievementId);

    List<UserAchievement> findByUserId(long userId);
}