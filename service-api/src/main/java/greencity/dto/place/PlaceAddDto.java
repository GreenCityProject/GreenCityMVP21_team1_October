package greencity.dto.place;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceAddDto {
    private CategoryDto category;
    private List<DiscountValueDto> discountValue;
    private LocationAddressAndGeoDto location;
    private String name;
    private List<OpeningHoursDto> openingHoursList;
    private List<PhotoAddDto> photos;
}
