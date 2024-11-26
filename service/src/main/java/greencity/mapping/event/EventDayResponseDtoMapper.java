package greencity.mapping.event;

import greencity.dto.event.EventDayResponseDto;
import greencity.entity.EventDay;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDayResponseDtoMapper extends AbstractConverter<EventDay, EventDayResponseDto> {
    @Override
    protected EventDayResponseDto convert(EventDay eventDay) {
        return EventDayResponseDto.builder()
                .id(eventDay.getId())
                .eventDate(eventDay.getEventDate())
                .eventStartTime(eventDay.getEventStartTime())
                .eventEndTime(eventDay.getEventEndTime())
                .isOnline(eventDay.getIsOnline())
                .onlineLink(eventDay.getOnlineLink())
                .longitude(eventDay.getLongitude())
                .latitude(eventDay.getLatitude())
                .build();
    }
}

