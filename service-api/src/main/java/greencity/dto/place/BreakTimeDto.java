package greencity.dto.place;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BreakTimeDto {
    private String startTime;
    private String endTime;
}
