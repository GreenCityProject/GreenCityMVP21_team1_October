package greencity.mapping;

import greencity.dto.place.BreakTimeDto;
import greencity.dto.place.OpeningHoursDto;
import greencity.entity.OpenHours;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class OpeningHoursDtoMapper extends AbstractConverter<OpenHours, OpeningHoursDto> {

    @Override
    protected OpeningHoursDto convert(OpenHours openHours) {
        return OpeningHoursDto.builder()
                .weekDay(openHours.getWeekDay())
                .openTime(openHours.getOpenTime().toLocalTime())
                .closeTime(openHours.getCloseTime().toLocalTime())
                .breakTime(openHours.getBreakTime() == null ? null : BreakTimeDto.builder()
                        .startTime(openHours.getBreakTime().getStartTime().toString())
                        .endTime(openHours.getBreakTime().getEndTime().toString())
                        .build())
                .build();
    }
}
