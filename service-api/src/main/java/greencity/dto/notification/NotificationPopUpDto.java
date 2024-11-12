package greencity.dto.notification;

import greencity.enums.NotificationOrigin;
import greencity.enums.NotificationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class NotificationPopUpDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    private NotificationOrigin notificationOrigin;

    @NotEmpty
    private String description;

    @NotEmpty
    private String content;

}
