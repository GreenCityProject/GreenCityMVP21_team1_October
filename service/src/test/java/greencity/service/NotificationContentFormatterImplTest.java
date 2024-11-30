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
        userVO.setName("Іван");

        EventVO eventVO = new EventVO();
        eventVO.setTitle("Тестова подія");

        LocalDateTime commentDate = LocalDateTime.of(2024, 11, 30, 15, 30);

        String expectedOutput = "Іван коментував вашу подію Тестова подія. Сьогодні 03:30 пп";
        String actualOutput = notificationContentFormatter.formatEventCommentNotification(userVO, eventVO, commentDate);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testFormatEventCommentNotification_ExceedsEventNameLength() {
        UserVO userVO = new UserVO();
        userVO.setName("Марія");

        EventVO eventVO = new EventVO();
        eventVO.setTitle("Довга назва події, яка перевищує двадцять символів");

        LocalDateTime commentDate = LocalDateTime.of(2024, 11, 29, 10, 45);

        String expectedOutput = "Марія коментував вашу подію Довга назва події.... Вчора 10:45 дп";
        String actualOutput = notificationContentFormatter.formatEventCommentNotification(userVO, eventVO, commentDate);

        assertEquals(expectedOutput, actualOutput);
    }
}