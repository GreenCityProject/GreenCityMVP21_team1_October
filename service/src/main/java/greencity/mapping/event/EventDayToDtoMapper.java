package greencity.mapping.event;

import greencity.dto.event.EventDayDto;
import greencity.dto.location.LocationDto;
import greencity.entity.EventDay;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EventDayToDtoMapper extends AbstractConverter<EventDay, EventDayDto> {

    @Override
    protected EventDayDto convert(EventDay source) {
        LocationDto locationDto = source.getLocation() != null
                ? LocationDto.builder()
                .id(source.getLocation().getId())
                .lat(source.getLocation().getLat())
                .lng(source.getLocation().getLng())
                .address(source.getLocation().getAddress())
                .build()
                : null;

        return EventDayDto.builder()
                .id(source.getId())
                .eventDate(source.getEventDate())
                .eventStartTime(source.getEventStartTime())
                .eventEndTime(source.getEventEndTime())
                .location(locationDto)
                .isOnline(source.getIsOnline())
                .onlineLink(source.getOnlineLink())
                .build();
    }
}
