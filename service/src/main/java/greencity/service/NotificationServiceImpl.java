package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final ModelMapper modelMapper;

    /**
     * Method to get user`s notifications.
     *
     * @param userVO addressee of the notifications.
     * @return list of {@link NotificationPopUpDto}.
     */
    @Override
    public List<NotificationPopUpDto> getPopUpNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndMarkedAsRead(id, false);
        return getNotificationPopUpDtoList(notifications);
    }

    /**
     * Method to get user`s notifications.
     *
     * @param userVO addressee of the notifications.
     * @return list of {@link NotificationDto}.
     */
    @Override
    public List<NotificationDto> getNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserId(id);
        return getNotificationDtoList(notifications);
    }

    /**
     * Method to get user`s notifications filtered by notification type.
     *
     * @param userVO user whose notifications are being filtered.
     * @param notificationType {@link NotificationType} the notification type.
     * @return list of {@link NotificationDto} filtered by notification type.
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationType(UserVO userVO, NotificationType notificationType) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationType(id, notificationType);
        return getNotificationDtoList(notifications);
    }

    /**
     * Method to get user`s notifications filtered by notification origin.
     *
     * @param userVO user whose notifications are being filtered.
     * @param notificationOrigin {@link NotificationOrigin} the notification origin.
     * @return list of {@link NotificationDto} filtered by notification origin.
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationOrigin(UserVO userVO, NotificationOrigin notificationOrigin) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationOrigin(id, notificationOrigin);
        return getNotificationDtoList(notifications);
    }

    /**
     * Method for deleting the notification.
     *
     * @param notificationId id of the notification being deleted.
     */
    @Override
    public void delete(Long notificationId) {
        notificationRepo.deleteById(notificationId);
    }

    /**
     * Method for marking notification as read.
     *
     * @param notificationId id of the notification being marked read.
     */
    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + notificationId));

        notification.setMarkedAsRead(true);
        notificationRepo.save(notification);
    }

    /**
     * Method for marking notification as unread.
     *
     * @param notificationId id of the notification being marked unread.
     */
    @Override
    public void markAsUnread(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + notificationId));

        notification.setMarkedAsRead(false);
        notificationRepo.save(notification);
    }

    /**
     * Method for sending notification about a reply to the eco news comment`s author.
     *
     * @param ecoNewsCommentVO eco news comment being replied to.
     * @param userVO {@link UserVO} user that is replying to the comment.
     * @author Max Kozak
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
     * Method for sending notification about eco news comment being liked to the eco news comment`s author.
     *
     * @param ecoNewsCommentVO eco news comment being liked.
     * @param userVO {@link UserVO} that wants to like the comment.
     * @author Max Kozak
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
     * Method for sending notification about eco news being liked to eco news` author.
     * @param userVO {@link UserVO} that likes news.
     * @param ecoNewsVO eco news being liked.
     * @author Max Kozak
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
     * Method for sending notification about eco news being commented to the eco news` author.
     *
     * @param ecoNewsVO eco news being commented.
     * @param userVO {@link UserVO} that saves the comment.
     * @author Max Kozak
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
     * Method for sending notification to user by user`s id.
     *
     * @param userId id of the user who receives the notification.
     * @param notificationOrigin the notification origin.
     * @param notificationType the notification type.
     * @param content the detailed content of the notification
     * @author Max Kozak
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
     * Method for getting all notification types
     *
     * @return array of {@link NotificationType}
     */
    @Override
    public NotificationType[] getNotificationTypes() {
        return NotificationType.values();
    }

    /**
     * Method for getting all notification origins
     *
     * @return array of {@link NotificationOrigin}
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
}
