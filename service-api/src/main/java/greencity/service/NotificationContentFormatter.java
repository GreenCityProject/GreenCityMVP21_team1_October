package greencity.service;

import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;

import java.time.LocalDateTime;

public interface NotificationContentFormatter {
    String formatEventCommentNotification(UserVO user, EventVO event, LocalDateTime commentDate);
}