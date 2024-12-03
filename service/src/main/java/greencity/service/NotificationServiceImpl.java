package greencity.service;

import ch.qos.logback.core.model.ModelUtil;
import greencity.constant.ErrorMessage;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.event.EventVO;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationPopUpDto;
import greencity.dto.user.UserVO;
import greencity.entity.Notification;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final ModelMapper modelMapper;


    @Scheduled(cron = "0 0 0 */7 * *")
    public void scheduledDeleteOfReadNotifications() {
        List<Notification> notifications = notificationRepo.findAll();
        notifications.forEach(notification -> {
            Date createdAt = notification.getCreatedAt();
            Date now = new Date();

            Calendar createdAtCalendar = Calendar.getInstance();
            createdAtCalendar.setTime(createdAt);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);

            if(!notification.isMarkedAsRead()) {
                return;
            }

            if(calendar.getWeekYear() > createdAtCalendar.getWeekYear()) {
                notificationRepo.delete(notification);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationPopUpDto> getPopUpNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndMarkedAsRead(id, false);
        return getNotificationPopUpDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDto> getNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserId(id);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationType(UserVO userVO, NotificationType notificationType) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationType(id, notificationType);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationOrigin(UserVO userVO, NotificationOrigin notificationOrigin) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationOrigin(id, notificationOrigin);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long notificationId) {
        notificationRepo.deleteById(notificationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + notificationId));

        notification.setMarkedAsRead(true);
        notificationRepo.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsUnread(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + notificationId));

        notification.setMarkedAsRead(false);
        notificationRepo.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEcoNewsCommentRepliedNotification(EcoNewsCommentVO ecoNewsCommentVO, UserVO userVO) {
        UserVO author = ecoNewsCommentVO.getUser();
        Long authorId = author.getId();

        EcoNewsVO ecoNewsVO = ecoNewsCommentVO.getEcoNews();
        String ecoNewsTitle = ecoNewsVO.getTitle();

        String replierName = userVO.getName();

        String content = replierName + " replied to your comment under " + ecoNewsTitle + " news";

        save(
                authorId,
                NotificationOrigin.GREEN_CITY,
                NotificationType.COMMENT_REPLY,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEcoNewsCommentLikedNotification(EcoNewsCommentVO ecoNewsCommentVO, UserVO userVO) {
        UserVO author = ecoNewsCommentVO.getUser();
        Long authorId = author.getId();

        EcoNewsVO ecoNewsVO = ecoNewsCommentVO.getEcoNews();
        String ecoNewsTitle = ecoNewsVO.getTitle();

        String userThatWantsToLikeName = userVO.getName();

        String content = "Your comment under " + ecoNewsTitle + " news was liked by " + userThatWantsToLikeName;

        save(
                authorId,
                NotificationOrigin.GREEN_CITY,
                NotificationType.COMMENT_LIKE,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEcoNewsLikedNotification(EcoNewsVO ecoNewsVO, UserVO userVO) {
        UserVO author = ecoNewsVO.getAuthor();
        Long authorId = author.getId();

        String ecoNewsTitle = ecoNewsVO.getTitle();
        String userThatWantsToLikeName = userVO.getName();

        String content = "Your eco news " + ecoNewsTitle + " were liked by " + userThatWantsToLikeName;

        save(
                authorId,
                NotificationOrigin.GREEN_CITY,
                NotificationType.ECO_NEWS_LIKE,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEcoNewsCommentedNotification(EcoNewsVO ecoNewsVO, UserVO userVO) {
        UserVO author = ecoNewsVO.getAuthor();
        Long authorId = author.getId();

        String commentatorName = userVO.getName();
        String ecoNewsTitle = ecoNewsVO.getTitle();

        String content = "Your eco news " + ecoNewsTitle + " were commented by " + commentatorName;

        save(
                authorId,
                NotificationOrigin.GREEN_CITY,
                NotificationType.ECO_NEWS_COMMENTED,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendFriendRequestReceivedNotification(UserVO sender, UserVO recipient) {
        String content = "You got a friend request from " + sender.getName();

        save(
                recipient.getId(),
                NotificationOrigin.GREEN_CITY,
                NotificationType.FRIEND_REQUEST_RECEIVED,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendFriendRequestAcceptedNotification(UserVO sender, UserVO recipient) {
        String content = "Your friend request was accepted by " + recipient.getName();

        save(
                sender.getId(),
                NotificationOrigin.GREEN_CITY,
                NotificationType.FRIEND_REQUEST_ACCEPTED,
                content
        );
    }

    @Override
    public void sendFriendRequestDeclinedNotification(UserVO sender, UserVO recipient) {
        String content = "Your friend request was declined by " + recipient.getName();

        save(
                sender.getId(),
                NotificationOrigin.GREEN_CITY,
                NotificationType.FRIEND_REQUEST_DECLINED,
                content
        );
    }

    /**
     * {@inheritDoc}
     */
    public void save(
            Long userId,
            NotificationOrigin notificationOrigin,
            NotificationType notificationType,
            String content
    ) {
        Date now = new Date();
        boolean markedAsRead = false;
        String description = notificationType.getNotificationDescription();

        Notification notification = new Notification(
                userId,
                now,
                notificationOrigin,
                notificationType,
                description,
                content,
                markedAsRead
        );
        notificationRepo.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationType[] getNotificationTypes() {
        return NotificationType.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationOrigin[] getNotificationOrigins() {
        return NotificationOrigin.values();
    }

    private List<NotificationDto> getNotificationDtoList(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .toList();
    }

    private List<NotificationPopUpDto> getNotificationPopUpDtoList(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationPopUpDto.class))
                .toList();
    }

    @Override
    public void sendCancellationNotification(EventVO event, UserVO user) {
        String formattedDateTime = NotificationContentFormatterImpl.formatCancellationDateTime(new Date());
        String truncatedEventName = NotificationContentFormatterImpl.truncateEventName(event.getTitle());
        String content = "The event \"" + truncatedEventName + "\" scheduled for " + formattedDateTime + " was cancelled.";

        save(
                user.getId(),
                NotificationOrigin.GREEN_CITY,
                NotificationType.EVENT_CANCELLED,
                content
        );
    }

    @Override
    public void sendEventUpdateNotifications(EventVO oldEvent, EventVO newEvent, List<UserVO> users) {
        String content = null;
        String formattedDateTime = NotificationContentFormatterImpl.formatCancellationDateTime(new Date());

        if (!oldEvent.getTitle().equals(newEvent.getTitle())) {
            content = "Event \"" + oldEvent.getTitle() + "\" was updated. New name is " + newEvent.getTitle() + ". " + formattedDateTime;
        }

        if (content != null && !content.isEmpty()) {
            String finalContent = content;
            users.forEach(user -> {
                save(
                        user.getId(),
                        NotificationOrigin.GREEN_CITY,
                        NotificationType.EVENT_UPDATED,
                        finalContent
                );
            });
        }
    }
}
