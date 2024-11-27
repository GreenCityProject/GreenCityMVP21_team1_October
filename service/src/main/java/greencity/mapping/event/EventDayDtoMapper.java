package greencity.mapping.event;

import greencity.dto.event.EventDayCreateRequestDto;
import greencity.entity.EventDay;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EventDayDtoMapper extends AbstractConverter<EventDayCreateRequestDto, EventDay> {

    @Override
    protected EventDay convert(EventDayCreateRequestDto source) {
        return EventDay.builder()
                .eventDate(source.getEventDate())
                .eventStartTime(source.getEventStartTime())
                .eventEndTime(source.getEventEndTime())
                .longitude(source.getLongitude())
                .latitude(source.getLatitude())
                .onlineLink(source.getOnlineLink())
                .isOnline(source.getIsOnline())
                .build();
    }
}
