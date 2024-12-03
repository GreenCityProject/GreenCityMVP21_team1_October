package greencity.dto.place;

import greencity.dto.category.CategoryDto;
import greencity.dto.location.LocationDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PlaceUpdateDto {
    /**
     * Unique identifier of the place to update.
     */
    @NotNull(message = "Place ID cannot be null")
    private Long id;

    /**
     * Name of the place.
     */
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    private String name;

    /**
     * Location details of the place.
     */
    @NotNull(message = "Location details are required")
    private LocationDto location;

    /**
     * Category of the place.
     */
    @NotNull(message = "Category is required")
    private CategoryDto category;

    /**
     * List of opening hours for the place.
     */
    @NotNull(message = "Opening hours list cannot be null")
    @Size(min = 1, message = "There must be at least one opening hour entry")
    private List<OpeningHoursDto> openingHoursList;

    /**
     * List of discount values applicable to the place.
     */
    @NotNull(message = "Discount values list cannot be null")
    private List<DiscountValueDto> discountValues;

    /**
     * Additional address and geolocation data for updating location.
     */
    private LocationAddressAndGeoForUpdateDto locationAddressAndGeoForUpdate;
}
