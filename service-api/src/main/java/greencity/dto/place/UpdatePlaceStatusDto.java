package greencity.dto.place;

import greencity.enums.PlaceStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePlaceStatusDto {
    private Long id;
    private PlaceStatus status;
}
