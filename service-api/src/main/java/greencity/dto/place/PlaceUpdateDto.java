package greencity.dto.place;

import greencity.dto.category.CategoryDto;
import greencity.dto.location.LocationDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PlaceUpdateDto {
    private Long id;
    private String name;
    private LocationDto location;
    private CategoryDto category;
    private List<OpeningHoursDto> openingHoursList;
    private List<DiscountValueDto> discountValues;
    private LocationAddressAndGeoForUpdateDto locationAddressAndGeoForUpdate;
}
