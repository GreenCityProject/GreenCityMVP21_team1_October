package greencity.mapping.event;

import greencity.dto.event.EventDayCreateRequestDto;
import greencity.entity.EventDay;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EventDayToDtoMapper extends AbstractConverter<EventDay, EventDayCreateRequestDto> {
    @Override
    protected EventDayCreateRequestDto convert(EventDay source) {
        return EventDayCreateRequestDto.builder()
                .eventDate(source.getEventDate())
                .eventStartTime(source.getEventStartTime())
                .eventEndTime(source.getEventEndTime())
                .latitude(source.getLatitude())
                .longitude(source.getLongitude())
                .isOnline(source.getIsOnline())
                .onlineLink(source.getOnlineLink())
                .build();
    }
}
