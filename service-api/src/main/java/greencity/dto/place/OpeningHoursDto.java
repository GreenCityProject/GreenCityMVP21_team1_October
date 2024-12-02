package greencity.dto.place;

import greencity.enums.WeekDay;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OpeningHoursDto {
    private WeekDay weekDay;
    private LocalTime openTime;
    private LocalTime closeTime;
    private BreakTimeDto breakTime;
}
