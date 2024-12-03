package greencity.dto.event;

import greencity.dto.location.LocationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class EventDayDto {
    @Schema(example = "1", description = "Unique identifier for the event day")
    private Long id;

    @Schema(example = "2024-11-09", description = "Date of the event day")
    private LocalDate eventDate;

    @Schema(example = "16:00:00", description = "Start time for the event")
    private LocalTime eventStartTime;

    @Schema(example = "18:00:00", description = "End time for the event")
    private LocalTime eventEndTime;

    @Schema(description = "Whether the event is online", example = "false")
    @NonNull
    private Boolean isOnline;

    @Schema(example = "https://example.com/event-link", description = "Online link for virtual events")
    private String onlineLink;

    private LocationDto location;
}

