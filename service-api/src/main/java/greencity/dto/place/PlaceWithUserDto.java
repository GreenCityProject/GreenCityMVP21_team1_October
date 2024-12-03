package greencity.dto.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceWithUserDto {
    private PlaceAuthorDto author;
    private CategoryDto category;
    private List<DiscountValueDto> discountValue;
    private Long id;
    private LocationAddressAndGeoDto location;
    private String name;
    private List<OpeningHoursDto> openingHoursList;
    private List<PhotoAddDto> photos;
    private String status;
}
