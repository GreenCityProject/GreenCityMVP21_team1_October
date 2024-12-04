package greencity.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    COMMENT_LIKE("Your comment was liked"),
    COMMENT_REPLY("Someone replied to your comment"),
    ECO_NEWS_LIKE("Your eco news received a like"),
    ECO_NEWS_COMMENTED("Your eco news received a comment"),
    EVENT_CREATED("Your friend created an event"),
    EVENT_CANCELLED("Event was cancelled"),
    EVENT_JOINED("Someone joined your event"),
    EVENT_UPDATED("Your event has been updated"),
    EVENT_COMMENTED("Your event received a comment"),
    FRIEND_REQUEST_RECEIVED("You received a new friend request"),
    FRIEND_REQUEST_ACCEPTED("Your friend request was accepted"),
    FRIEND_REQUEST_DECLINED("Your friend request was declined");

    private final String notificationDescription;
    NotificationType(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }
}
