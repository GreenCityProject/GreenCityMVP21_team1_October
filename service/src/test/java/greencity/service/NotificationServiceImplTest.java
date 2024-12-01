package greencity.service;

import greencity.ModelUtils;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.event.EventVO;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationPopUpDto;
import greencity.dto.user.UserVO;
import greencity.entity.Notification;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import greencity.repository.NotificationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Date;
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
        Long notificationId = 1L;
        notificationServiceImpl.delete(notificationId);
        verify(notificationRepo, times(1)).deleteById(notificationId);
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
    void markAsUnreadTest() {
        Notification notification = ModelUtils.getNotification();
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationServiceImpl.markAsUnread(notificationId);

        assertFalse(notification.isMarkedAsRead());
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
    void sendCancellationNotificationTest() {
        EventVO eventVO = new EventVO();
        eventVO.setTitle("Test Event");

        UserVO userVO = new UserVO();
        userVO.setId(1L);

        notificationServiceImpl.sendCancellationNotification(eventVO, userVO);

        verify(notificationRepo, times(1)).save(any());
    }
    @Test
    void sendCancellationNotificationTest_VerifyContent() {
        EventVO eventVO = new EventVO();
        eventVO.setTitle("Test Event");

        UserVO userVO = new UserVO();
        userVO.setId(1L);

        notificationServiceImpl.sendCancellationNotification(eventVO, userVO);

        verify(notificationRepo, times(1)).save(Mockito.argThat(notification ->
                notification.getContent().contains("The event \"Test Event\" scheduled for") &&
                        notification.getContent().contains("was cancelled.")
        ));
    }
}