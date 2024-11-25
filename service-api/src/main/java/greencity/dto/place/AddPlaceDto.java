package greencity.dto.place;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPlaceDto {
    private String categoryName;
    private String locationName;
    private List<OpeningHoursDto> openingHoursList;
    private String placeName;
}
