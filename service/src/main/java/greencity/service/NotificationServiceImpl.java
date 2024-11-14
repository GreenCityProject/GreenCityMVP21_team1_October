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
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public List<NotificationPopUpDto> getPopUpNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndMarkedAsRead(id, false);
        return getNotificationPopUpDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public List<NotificationDto> getNotificationsByUser(UserVO userVO) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserId(id);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationType(UserVO userVO, NotificationType notificationType) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationType(id, notificationType);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public List<NotificationDto> getNotificationsByUserAndNotificationOrigin(UserVO userVO, NotificationOrigin notificationOrigin) {
        Long id = userVO.getId();
        List<Notification> notifications = notificationRepo.findAllByUserIdAndNotificationOrigin(id, notificationOrigin);
        return getNotificationDtoList(notifications);
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public void delete(Long notificationId) {
        notificationRepo.deleteById(notificationId);
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
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
     *
     * @author Max Kozak
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
     *
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
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
     * @author Max Kozak
     */
    @Override
    public NotificationType[] getNotificationTypes() {
        return NotificationType.values();
    }

    /**
     * {@inheritDoc}
     *
     * @author Max Kozak
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
