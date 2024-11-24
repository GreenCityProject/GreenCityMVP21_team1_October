package greencity.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MyEventsResponseDto {
    private List<EventResponseDto> createdEvents;
    private List<EventResponseDto> attendingEvents;
}
