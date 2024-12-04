package greencity.repository;

import greencity.entity.Notification;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserId(Long userId);

    List<Notification> findAllByUserIdAndNotificationType(Long userId, NotificationType notificationType);

    List<Notification> findAllByUserIdAndNotificationOrigin(Long userId, NotificationOrigin notificationOrigin);

    List<Notification> findAllByUserIdAndMarkedAsRead(Long userId, Boolean markedAsRead);
}
