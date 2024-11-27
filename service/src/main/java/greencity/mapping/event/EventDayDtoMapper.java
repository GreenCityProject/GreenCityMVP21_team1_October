package greencity.mapping.event;

import greencity.dto.event.EventDayDto;
import greencity.entity.EventDay;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDayDtoMapper extends AbstractConverter<EventDayDto, EventDay> {

    @Override
    protected EventDay convert(EventDayDto source) {
        return EventDay.builder()
                .id(source.getId())
                .eventDate(source.getEventDate())
                .eventStartTime(source.getEventStartTime())
                .eventEndTime(source.getEventEndTime())
                .onlineLink(source.getOnlineLink())
                .isOnline(source.getIsOnline())
                .build();
    }
}
