package greencity.dto.place;

import greencity.enums.PlaceStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BulkUpdatePlaceStatusDto {
    private List<Long> ids;
    private PlaceStatus status;
}
