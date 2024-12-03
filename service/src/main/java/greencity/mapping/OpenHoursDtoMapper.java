package greencity.mapping;

import greencity.dto.place.BreakTimeDto;
import greencity.dto.place.OpenHoursDto;
import greencity.entity.OpenHours;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class OpenHoursDtoMapper extends AbstractConverter<OpenHours, OpenHoursDto> {

    @Override
    protected OpenHoursDto convert(OpenHours openHours) {
        return OpenHoursDto.builder()
                .id(openHours.getId())
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
