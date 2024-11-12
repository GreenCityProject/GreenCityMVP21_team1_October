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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/byUser/popUp")
    public ResponseEntity<List<NotificationPopUpDto>> getPopUpNotificationsByUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getPopUpNotificationsByUser(userVO)
                );
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationsByUser(userVO)
                );
    }

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

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long notificationId
    ) {
        notificationService.delete(notificationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<Object> markAsRead(
            @PathVariable Long notificationId
    ) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

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
