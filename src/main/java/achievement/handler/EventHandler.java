package achievement.handler;

public interface EventHandler <T> {
    void handle(T event);
    String getAchievementTitle();
}
