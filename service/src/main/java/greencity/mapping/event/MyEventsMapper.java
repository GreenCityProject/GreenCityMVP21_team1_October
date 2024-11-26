package greencity.mapping.event;// New MyEventsMapper class
import greencity.dto.event.EventResponseDto;
import greencity.entity.Event;
import greencity.entity.EventDay;
import greencity.dto.event.EventDayDto;
import greencity.entity.Tag;
import greencity.dto.tag.TagUaEnDto;
import greencity.entity.EventImages;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class MyEventsMapper {

    public EventResponseDto toEventResponseDto(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .isOpen(event.getIsOpen())
                .type(event.getType())
                .image(event.getImage())
                .dayList(event.getEventDays().stream()
                        .map(this::convertToEventDayDto)
                        .collect(Collectors.toList()))
                .additionalImages(event.getAdditionalImages().stream()
                        .map(EventImages::getLink)
                        .collect(Collectors.toList()))
                .tags(event.getTags().stream()
                        .map(this::convertToTagUaEnDto)
                        .collect(Collectors.toList()))
                .status(event.getStatus())
                .build();
    }

    private EventDayDto convertToEventDayDto(EventDay eventDay) {
        return EventDayDto.builder()
                .id(eventDay.getId())
                .eventDate(eventDay.getEventDate())
                .eventStartTime(eventDay.getEventStartTime())
                .eventEndTime(eventDay.getEventEndTime())
                .latitude(eventDay.getLatitude())
                .longitude(eventDay.getLongitude())
                .isOnline(eventDay.getIsOnline())
                .onlineLink(eventDay.getOnlineLink())
                .build();
    }

    private TagUaEnDto convertToTagUaEnDto(Tag tag) {
        return TagUaEnDto.builder()
                .id(tag.getId())
                .nameEn(tag.getTagTranslations().stream()
                        .filter(translation -> translation.getLanguage().getCode().equals("en"))
                        .findFirst()
                        .orElseThrow()
                        .getName())
                .nameUa(tag.getTagTranslations().stream()
                        .filter(translation -> translation.getLanguage().getCode().equals("ua"))
                        .findFirst()
                        .orElseThrow()
                        .getName())
                .build();
    }
}