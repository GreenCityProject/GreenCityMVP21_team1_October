package greencity.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventRequestDto {

    @Schema(example = "Community Cleanup", description = "Title of the event")
    @NotBlank
    private String title;

    @Schema(example = "Join us for a community cleanup!", description = "Description of the event")
    @Size(min = 20, max = 63206)
    @NotBlank
    private String description;

    @Schema(description = "List of event days with start/end times and location info")
    @Size(max = 7)
    @NotBlank
    private List<EventDayCreateRequestDto> eventDays = new ArrayList<>();

    @Schema(description = "Whether the event is open to the public", example = "true")
    private Boolean isOpenEvent;

    @Schema(description = "Whether the event lasts all day", example = "false")
    private Boolean allDayEvent;

    @Schema(example = "Environmental")
    private List<String> tags;


}
