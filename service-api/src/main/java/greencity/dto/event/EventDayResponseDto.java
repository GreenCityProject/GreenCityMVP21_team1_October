package greencity.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDayResponseDto {

    Long id;

    @Schema(example = "2024-11-09", description = "Date of the event day")
    private LocalDate eventDate;

    @Schema(example = "16:00:00", description = "Start time for the event")
    private LocalTime eventStartTime;

    @Schema(example = "18:00:00", description = "End time for the event")
    private LocalTime eventEndTime;

    @Schema(example = "37.7749", description = "Latitude for the event location")
    private Double latitude;

    @Schema(example = "-122.4194", description = "Longitude for the event location")
    private Double longitude;

    @Schema(description = "Whether the event is online", example = "false")
    private Boolean isOnline;

    @Schema(example = "https://example.com/event-link", description = "Online link for virtual events")
    private String onlineLink;
}
