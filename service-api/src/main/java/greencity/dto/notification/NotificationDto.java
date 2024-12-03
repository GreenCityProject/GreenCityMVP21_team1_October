package greencity.dto.notification;

import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class NotificationDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    private NotificationOrigin notificationOrigin;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private Date createdAt;

    @NotEmpty
    private String description;

    @NotEmpty
    private String content;

    @NotNull
    private boolean markedAsRead;

}
