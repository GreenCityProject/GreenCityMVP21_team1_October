package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationPopUpDto;
import greencity.dto.user.UserVO;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import greencity.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
     */
    @Operation(summary = "Get all notification types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/notificationTypes")
    public ResponseEntity<NotificationType[]> getNotificationTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationTypes());
    }

    /**
     * Method for getting all notification origins
     *
     * @return array of {@link NotificationOrigin}
     */
    @Operation(summary = "Get all notification origins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/notificationOrigins")
    public ResponseEntity<NotificationOrigin[]> getNotificationOrigins() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationOrigins());
    }

    /**
     * Method to get user`s notifications showed in pop up notifications window.
     *
     * @return list of {@link NotificationPopUpDto}.
     */
    @Operation(summary = "Get user`s notifications showed in pop-up window")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/popUp")
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
     */
    @Operation(summary = "Get user`s notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping
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
     */
    @Operation(summary = "Find notifications by notification type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/search/notificationType")
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
     */
    @Operation(summary = "Find notifications by notification origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/search/notificationOrigin")
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
     */
    @Operation(summary = "Delete notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
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
     */
    @Operation(summary = "Mark notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
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
     */
    @Operation(summary = "Mark notification as unread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
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
