package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationPopUpDto;
import greencity.dto.user.UserVO;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import greencity.service.NotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Method for getting all notification types
     *
     * @return array of {@link NotificationType}
     * @author Max Kozak
     */
    @GetMapping("/notificationTypes")
    public ResponseEntity<NotificationType[]> getNotificationTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationTypes());
    }

    /**
     * Method for getting all notification origins
     *
     * @return array of {@link NotificationOrigin}
     * @author Max Kozak
     */
    @GetMapping("/notificationOrigins")
    public ResponseEntity<NotificationOrigin[]> getNotificationOrigins() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationOrigins());
    }

    /**
     * Method to get user`s notifications showed in pop up notifications window.
     *
     * @return list of {@link NotificationPopUpDto}.
     * @author Max Kozak
     */
    @GetMapping("/byUser/popUp")
    public ResponseEntity<List<NotificationPopUpDto>> getPopUpNotificationsByUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getPopUpNotificationsByUser(userVO)
                );
    }

    /**
     * Method to get user`s notifications.
     *
     * @return list of {@link NotificationDto}.
     * @author Max Kozak
     */
    @GetMapping("/byUser")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationsByUser(userVO)
                );
    }

    /**
     * Method to get user`s notifications filtered by notification type.
     *
     * @param notificationType {@link NotificationType} the notification type.
     * @return list of {@link NotificationDto} filtered by notification type.
     * @author Max Kozak
     */
    @GetMapping("/byUser/byNotificationType")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserAndNotificationType(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @RequestParam NotificationType notificationType
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationsByUserAndNotificationType(userVO, notificationType)
                );
    }

    /**
     * Method to get user`s notifications filtered by notification origin.
     *
     * @param notificationOrigin {@link NotificationOrigin} the notification origin.
     * @return list of {@link NotificationDto} filtered by notification origin.
     * @author Max Kozak
     */
    @GetMapping("/byUser/byNotificationOrigin")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserAndNotificationOrigin(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @RequestParam NotificationOrigin notificationOrigin
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationsByUserAndNotificationOrigin(userVO, notificationOrigin)
                );
    }

    /**
     * Method for deleting the notification.
     *
     * @param notificationId id of the notification being deleted.
     * @author Max Kozak
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long notificationId
    ) {
        notificationService.delete(notificationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Method for marking notification as read.
     *
     * @param notificationId id of the notification being marked read.
     * @author Max Kozak
     */
    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<Object> markAsRead(
            @PathVariable Long notificationId
    ) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Method for marking notification as unread.
     *
     * @param notificationId id of the notification being marked unread.
     * @author Max Kozak
     */
    @PatchMapping("/unread/{notificationId}")
    public ResponseEntity<Object> markAsUnread(
            @PathVariable Long notificationId
    ) {
        notificationService.markAsUnread(notificationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
