package greencity.service;

import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationPopUpDto;
import greencity.dto.user.UserVO;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    /**
     * Method to get user`s notifications showed in pop up notifications window.
     *
     * @param userVO addressee of the notifications.
     * @return list of {@link NotificationPopUpDto}.
     */
    List<NotificationPopUpDto> getPopUpNotificationsByUser(UserVO userVO);

    /**
     * Method to get user`s notifications.
     *
     * @param userVO addressee of the notifications.
     * @return list of {@link NotificationDto}.
     */
    List<NotificationDto> getNotificationsByUser(UserVO userVO);

    /**
     * Method to get user`s notifications filtered by notification type.
     *
     * @param userVO user whose notifications are being filtered.
     * @param notificationType {@link NotificationType} the notification type.
     * @return list of {@link NotificationDto} filtered by notification type.
     */
    List<NotificationDto> getNotificationsByUserAndNotificationType(UserVO userVO, NotificationType notificationType);

    /**
     * Method to get user`s notifications filtered by notification origin.
     *
     * @param userVO user whose notifications are being filtered.
     * @param notificationOrigin {@link NotificationOrigin} the notification origin.
     * @return list of {@link NotificationDto} filtered by notification origin.
     */
    List<NotificationDto> getNotificationsByUserAndNotificationOrigin(UserVO userVO, NotificationOrigin notificationOrigin);

    /**
     * Method for deleting the notification.
     *
     * @param notificationId id of the notification being deleted.
     */
    void delete(Long notificationId);

    /**
     * Method for marking notification as read.
     *
     * @param notificationId id of the notification being marked read.
     */
    void markAsRead(Long notificationId);

    /**
     * Method for marking notification as unread.
     *
     * @param notificationId id of the notification being marked unread.
     */
    void markAsUnread(Long notificationId);

    /**
     * Method for sending notification about a reply to the eco news comment`s author.
     *
     * @param ecoNewsCommentVO eco news comment being replied to.
     * @param userVO {@link UserVO} user that is replying to the comment.
     */
    void sendEcoNewsCommentRepliedNotification(EcoNewsCommentVO ecoNewsCommentVO, UserVO userVO);

    /**
     * Method for sending notification about eco news comment being liked to the eco news comment`s author.
     *
     * @param ecoNewsCommentVO eco news comment being liked.
     * @param userVO {@link UserVO} that wants to like the comment.
     */
    void sendEcoNewsCommentLikedNotification(EcoNewsCommentVO ecoNewsCommentVO, UserVO userVO);

    /**
     * Method for sending notification about eco news being liked to eco news` author.
     * @param userVO {@link UserVO} that likes news.
     * @param ecoNewsVO eco news being liked.
     */
    void sendEcoNewsLikedNotification(EcoNewsVO ecoNewsVO, UserVO userVO);

    /**
     * Method for sending notification about eco news being commented to the eco news` author.
     *
     * @param ecoNewsVO eco news being commented.
     * @param userVO {@link UserVO} that saves the comment.
     */
    void sendEcoNewsCommentedNotification(EcoNewsVO ecoNewsVO, UserVO userVO);

    /**
     * Method for sending notification about friend request was received by user.
     *
     * @param sender {@link UserVO} is the user who sent the friend request.
     * @param recipient {@link UserVO} is the user who received the friend request.
     */
    void sendFriendRequestReceivedNotification(UserVO sender, UserVO recipient);

    /**
     * Method for sending notification to user about his friend request was accepted.
     *
     * @param sender {@link UserVO} is the user who sent the friend request.
     * @param recipient {@link UserVO} is the new friend who accepted the friend request.
     */
    void sendFriendRequestAcceptedNotification(UserVO sender, UserVO recipient);

    /**
     * Method for sending notification to user about his friend request was declined.
     *
     * @param sender {@link UserVO} is the user who sent the friend request.
     * @param recipient {@link UserVO} is the new friend who declined the friend request.
     */
    void sendFriendRequestDeclinedNotification(UserVO sender, UserVO recipient);

    /**
     * Method for sending notification to user by user`s id.
     *
     * @param userId id of the user who receives the notification.
     * @param notificationOrigin the notification origin.
     * @param notificationType the notification type.
     * @param content the detailed content of the notification
     */
    void save(Long userId, NotificationOrigin notificationOrigin, NotificationType notificationType, String content);

    /**
     * Method for getting all notification types
     *
     * @return array of {@link NotificationType}
     */
    NotificationType[] getNotificationTypes();

    /**
     * Method for getting all notification origins
     *
     * @return array of {@link NotificationOrigin}
     */
    NotificationOrigin[] getNotificationOrigins();
}
