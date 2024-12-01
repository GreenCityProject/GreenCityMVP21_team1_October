package greencity.service;

import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class NotificationContentFormatterImpl implements NotificationContentFormatter {
    @Override
    public String formatEventCommentNotification(UserVO user, EventVO event, LocalDateTime commentDate) {
        String eventName = event.getTitle();
        if (eventName.length() > 20) {
            eventName = eventName.substring(0, 17) + "...";
        }
        String formattedDate = formatDate(commentDate);
        return user.getName() + " commented on your event" + eventName + ". " + formattedDate;
    }

    private String formatDate(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.toLocalDate().isEqual(now.toLocalDate())) {
            return "Today " + dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        } else if (dateTime.toLocalDate().isEqual(now.toLocalDate().minusDays(1))) {
            return "Yesterday " + dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a"));
        }
    }

    public static String formatCancellationDateTime(Date cancellationDateTime) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(cancellationDateTime);
    }

    public static String truncateEventName(String eventName) {
        return eventName.length() > 50 ? eventName.substring(0, 47) + "..." : eventName;
    }
}