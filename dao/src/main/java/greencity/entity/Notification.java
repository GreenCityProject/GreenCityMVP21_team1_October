package greencity.entity;

import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column(updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column
    private NotificationOrigin notificationOrigin;

    @Enumerated(EnumType.STRING)
    @Column
    private NotificationType notificationType;

    @Column
    private String description;

    @Column
    private String content;

    @Column
    private boolean markedAsRead;

    public Notification(Long userId,
                        Date createdAt,
                        NotificationOrigin notificationOrigin,
                        NotificationType notificationType,
                        String description,
                        String content,
                        boolean markedAsRead) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.notificationOrigin = notificationOrigin;
        this.notificationType = notificationType;
        this.description = description;
        this.content = content;
        this.markedAsRead = markedAsRead;
    }
}
