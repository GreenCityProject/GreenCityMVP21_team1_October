package greencity.dto.place;

import greencity.enums.PlaceStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BulkUpdatePlaceStatusDto {
    private List<Long> ids;
    private PlaceStatus status;
}
