package greencity.service;

import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class NotificationContentFormatterImplTest {

    @InjectMocks
    private NotificationContentFormatterImpl notificationContentFormatter;

    @Test
    void testFormatEventCommentNotification() {
        UserVO userVO = new UserVO();
        userVO.setName("Ivan");

        EventVO eventVO = new EventVO();
        eventVO.setTitle("Test event");

        LocalDateTime commentDate = LocalDateTime.of(2024, 11, 30, 15, 30);

        String expectedOutput = "Ivan commented on your eventTest event. Yesterday 03:30 пп";
        String actualOutput = notificationContentFormatter.formatEventCommentNotification(userVO, eventVO, commentDate);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testFormatEventCommentNotification_ExceedsEventNameLength() {
        UserVO userVO = new UserVO();
        userVO.setName("Maria");

        EventVO eventVO = new EventVO();
        eventVO.setTitle("A long event name that exceeds twenty characters");

        LocalDateTime commentDate = LocalDateTime.of(2024, 11, 29, 10, 45);

        String expectedOutput = "Maria commented on your eventA long event name.... 29.11.2024 10:45 дп";
        String actualOutput = notificationContentFormatter.formatEventCommentNotification(userVO, eventVO, commentDate);

        assertEquals(expectedOutput, actualOutput);
    }
}