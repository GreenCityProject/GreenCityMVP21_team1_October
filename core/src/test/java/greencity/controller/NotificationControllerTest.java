package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.converters.UserArgumentResolver;
import greencity.dto.user.UserVO;
import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.NotificationService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    private final String notificationsLink = "/notifications";

    private MockMvc mockMvc;

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(notificationController)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver(),
                        new UserArgumentResolver(userService, modelMapper)
                )
                .setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
                .alwaysDo(print())
                .build();
    }

    @Test
    void getPopUpNotificationsByUserTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(notificationsLink + "/popUp")
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(notificationService).getPopUpNotificationsByUser(userVO);
    }

    @Test
    void getNotificationsByUserTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(notificationsLink)
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(notificationService).getNotificationsByUser(userVO);
    }

    @Test
    void getNotificationsByUserAndNotificationTypeTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();
        NotificationType notificationType = NotificationType.COMMENT_LIKE;

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(notificationsLink + "/search/notificationType")
                        .param("notificationType", notificationType.name())
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(notificationService).getNotificationsByUserAndNotificationType(userVO, notificationType);
    }

    @Test
    void getNotificationsByUserAndNotificationOriginTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();
        NotificationOrigin notificationOrigin = NotificationOrigin.GREEN_CITY;

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(notificationsLink + "/search/notificationOrigin")
                        .param("notificationOrigin", notificationOrigin.name())
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(notificationService).getNotificationsByUserAndNotificationOrigin(userVO, notificationOrigin);
    }

    @Test
    void deleteTest() throws Exception {
        Long notificationId = 1L;

        mockMvc.perform(delete(notificationsLink + "/" + notificationId))
                .andExpect(status().isOk());

        verify(notificationService).delete(notificationId);
    }

    @Test
    void markAsReadTest() throws Exception {
        Long notificationId = 1L;

        mockMvc.perform(patch(notificationsLink + "/read/" + notificationId))
                .andExpect(status().isOk());

        verify(notificationService).markAsRead(notificationId);
    }

    @Test
    void markAsUnreadTest() throws Exception {
        Long notificationId = 1L;

        mockMvc.perform(patch(notificationsLink + "/unread/" + notificationId))
                .andExpect(status().isOk());

        verify(notificationService).markAsUnread(notificationId);
    }

    @Test
    void getNotificationTypesTest() throws Exception {
        mockMvc.perform(get(notificationsLink + "/notificationTypes"))
                .andExpect(status().isOk());

        verify(notificationService).getNotificationTypes();
    }

    @Test
    void getNotificationOriginsTest() throws Exception {
        mockMvc.perform(get(notificationsLink + "/notificationOrigins"))
                .andExpect(status().isOk());

        verify(notificationService).getNotificationOrigins();
    }

}
