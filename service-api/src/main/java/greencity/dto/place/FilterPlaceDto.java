package greencity.dto.place;

import greencity.dto.filter.FilterDiscountDto;
import greencity.dto.filter.FilterDistanceDto;
import greencity.enums.PlaceStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FilterPlaceDto {
    private List<String> categories;
    private FilterDiscountDto discountDto;
    private FilterDistanceDto distanceFromUserDto;
    private MapBoundsDto mapBoundsDto;
    private String searchReg;
    private PlaceStatus status;
    private String time;
}
