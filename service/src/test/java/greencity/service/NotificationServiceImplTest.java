package greencity.service;

import greencity.ModelUtils;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Test
    void getPopUpNotificationsByUserTest() {
        UserVO userVO = ModelUtils.getUserVO();

        NotificationPopUpDto notificationPopUpDto = ModelUtils.getNotificationPopUpDto();
        List<Notification> notifications = List.of(ModelUtils.getNotification());
        List<NotificationPopUpDto> notificationPopUpDtoList = List.of(notificationPopUpDto);

        when(notificationRepo.findAllByUserIdAndMarkedAsRead(eq(userVO.getId()), anyBoolean())).thenReturn(notifications);
        when(modelMapper.map(any(), eq(NotificationPopUpDto.class))).thenReturn(notificationPopUpDto);

        assertEquals(notificationServiceImpl.getPopUpNotificationsByUser(userVO), notificationPopUpDtoList);
    }

    @Test
    void getNotificationsByUserTest() {
        UserVO userVO = ModelUtils.getUserVO();

        NotificationDto notificationDto = ModelUtils.getNotificationDto();
        List<Notification> notifications = List.of(ModelUtils.getNotification());
        List<NotificationDto> notificationDtoList = List.of(notificationDto);

        when(notificationRepo.findAllByUserId(eq(userVO.getId()))).thenReturn(notifications);
        when(modelMapper.map(any(), eq(NotificationDto.class))).thenReturn(notificationDto);

        assertEquals(notificationServiceImpl.getNotificationsByUser(userVO), notificationDtoList);
    }

    @Test
    void getNotificationsByUserAndNotificationTypeTest() {
        UserVO userVO = ModelUtils.getUserVO();
        NotificationDto notificationDto = ModelUtils.getNotificationDto();
        Notification notification = ModelUtils.getNotification();

        NotificationType notificationType = notification.getNotificationType();

        List<Notification> notifications = List.of(notification);
        List<NotificationDto> notificationDtoList = List.of(notificationDto);

        when(notificationRepo.findAllByUserIdAndNotificationType(eq(userVO.getId()), eq(notificationType))).thenReturn(notifications);
        when(modelMapper.map(any(), eq(NotificationDto.class))).thenReturn(notificationDto);

        assertEquals(notificationServiceImpl.getNotificationsByUserAndNotificationType(userVO, notificationType), notificationDtoList);
    }

    @Test
    void getNotificationsByUserAndNotificationOriginTest() {
        UserVO userVO = ModelUtils.getUserVO();
        NotificationDto notificationDto = ModelUtils.getNotificationDto();
        Notification notification = ModelUtils.getNotification();

        NotificationOrigin notificationOrigin = notification.getNotificationOrigin();

        List<Notification> notifications = List.of(notification);
        List<NotificationDto> notificationDtoList = List.of(notificationDto);

        when(notificationRepo.findAllByUserIdAndNotificationOrigin(eq(userVO.getId()), eq(notificationOrigin))).thenReturn(notifications);
        when(modelMapper.map(any(), eq(NotificationDto.class))).thenReturn(notificationDto);

        assertEquals(notificationServiceImpl.getNotificationsByUserAndNotificationOrigin(userVO, notificationOrigin), notificationDtoList);
    }

    @Test
    void deleteTest() {
        Notification notification = ModelUtils.getNotification();
        Long notificationId = notification.getId();

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationServiceImpl.delete(notificationId);
        verify(notificationRepo, times(1)).delete(notification);
    }

    @Test
    void deleteTestThrowsNotFoundException() {
        Notification notification = ModelUtils.getNotification();
        Long notificationId = notification.getId();

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationServiceImpl.delete(notificationId));
    }

    @Test
    void markAsReadTest() {
        Notification notification = ModelUtils.getNotification();
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationServiceImpl.markAsRead(notificationId);

        assertTrue(notification.isMarkedAsRead());
    }

    @Test
    void markAsReadTestThrowsNotFoundException() {
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationServiceImpl.markAsRead(notificationId));
    }

    @Test
    void markAsUnreadTest() {
        Notification notification = ModelUtils.getNotification();
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationServiceImpl.markAsUnread(notificationId);

        assertFalse(notification.isMarkedAsRead());
    }

    @Test
    void markAsUnreadTestThrowsNotFoundException() {
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationServiceImpl.markAsUnread(notificationId));
    }

    @Test
    void sendEcoNewsCommentRepliedNotificationTest() {
        EcoNewsCommentVO ecoNewsCommentVO = ModelUtils.getEcoNewsCommentVO();
        UserVO userVO = ModelUtils.getUserVO();

        notificationServiceImpl.sendEcoNewsCommentRepliedNotification(ecoNewsCommentVO, userVO);

        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendEcoNewsCommentLikedNotificationTest() {
        EcoNewsCommentVO ecoNewsCommentVO = ModelUtils.getEcoNewsCommentVO();
        UserVO userVO = ModelUtils.getUserVO();

        notificationServiceImpl.sendEcoNewsCommentLikedNotification(ecoNewsCommentVO, userVO);

        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendEcoNewsLikedNotificationTest() {
        EcoNewsVO ecoNewsVO = ModelUtils.getEcoNewsVO();
        UserVO userVO = ModelUtils.getUserVO();

        notificationServiceImpl.sendEcoNewsLikedNotification(ecoNewsVO, userVO);

        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendEcoNewsCommentedNotificationTest() {
        EcoNewsVO ecoNewsVO = ModelUtils.getEcoNewsVO();
        UserVO userVO = ModelUtils.getUserVO();

        notificationServiceImpl.sendEcoNewsCommentedNotification(ecoNewsVO, userVO);
        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendFriendRequestReceivedNotificationTest() {
        UserVO sender = ModelUtils.getUserVO();
        UserVO recepient = ModelUtils.getUserVO();

        notificationServiceImpl.sendFriendRequestReceivedNotification(sender, recepient);
        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendFriendRequestAcceptedNotificationTest() {
        UserVO sender = ModelUtils.getUserVO();
        UserVO recepient = ModelUtils.getUserVO();

        notificationServiceImpl.sendFriendRequestAcceptedNotification(sender, recepient);
        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void sendFriendRequestDeclinedNotificationTest() {
        UserVO sender = ModelUtils.getUserVO();
        UserVO recepient = ModelUtils.getUserVO();

        notificationServiceImpl.sendFriendRequestDeclinedNotification(sender, recepient);
        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void saveTest() {
        Notification notification = ModelUtils.getNotification();
        notificationServiceImpl.save(
                notification.getId(),
                notification.getNotificationOrigin(),
                notification.getNotificationType(),
                notification.getContent()
        );

        verify(notificationRepo, times(1)).save(any());
    }

    @Test
    void getNotificationTypesTest() {
        NotificationType[] expected = NotificationType.values();
        NotificationType[] actual = notificationServiceImpl.getNotificationTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    void getNotificationOriginsTest() {
        NotificationOrigin[] expected = NotificationOrigin.values();
        NotificationOrigin[] actual = notificationServiceImpl.getNotificationOrigins();

        assertArrayEquals(expected, actual);
    }

    @Test
    void scheduleDeleteMarkedAsReadNotificationsTest() {
        List<Notification> notifications = List.of(ModelUtils.getNotificationReadyForScheduledDeletion(), ModelUtils.getNotification());
        int expectedAmountOfDeletedNotifications = 1;

        when(notificationRepo.findAll()).thenReturn(notifications);

        notificationServiceImpl.scheduleDeleteMarkedAsReadNotifications();

        verify(notificationRepo, times(expectedAmountOfDeletedNotifications)).delete(any());
    }
}
