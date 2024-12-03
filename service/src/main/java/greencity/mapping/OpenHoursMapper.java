package greencity.mapping;

import greencity.dto.place.OpeningHoursDto;
import greencity.entity.BreakTime;
import greencity.entity.OpenHours;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class OpenHoursMapper extends AbstractConverter<OpeningHoursDto, OpenHours> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    protected OpenHours convert(OpeningHoursDto source) {
        OpenHours model =  OpenHours.builder()
            .breakTime(BreakTime
                .builder()
                .startTime(Time.valueOf(source.getBreakTime().getStartTime() + ":00"))
                .endTime(Time.valueOf(source.getBreakTime().getEndTime() + ":00"))
                .build())
            .openTime(Time.valueOf(source.getOpenTime().format(formatter)))
            .closeTime(Time.valueOf(source.getCloseTime().format(formatter)))
            .weekDay(source.getWeekDay())
            .build();

        model.getBreakTime().setOpenHours(model);
        return model;
    }
}
