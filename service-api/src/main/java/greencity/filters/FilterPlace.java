package greencity.filters;

import greencity.enums.PlaceStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.*;

import static greencity.constant.ServiceValidationConstants.*;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterPlace {
    @Min(value = -90, message = LAT_MIN_VALIDATION)
    @Max(value = 90, message = LAT_MAX_VALIDATION)
    private Double northEastLat;

    @Min(value = -90, message = LAT_MIN_VALIDATION)
    @Max(value = 90, message = LAT_MAX_VALIDATION)
    private Double southWestLat;

    @Min(value = -180, message = LNG_MIN_VALIDATION)
    @Max(value = 180, message = LNG_MAX_VALIDATION)
    private Double northEastLng;

    @Min(value = -180, message = LNG_MIN_VALIDATION)
    @Max(value = 180, message = LNG_MAX_VALIDATION)
    private Double southWestLng;

    private String specificationName;

    @Positive
    private int discountMin;

    @Positive
    private int discountMax;

    @Min(value = -90, message = LAT_MIN_VALIDATION)
    @Max(value = 90, message = LAT_MAX_VALIDATION)
    private Double lat;

    @Min(value = -180, message = LNG_MIN_VALIDATION)
    @Max(value = 180, message = LNG_MAX_VALIDATION)
    private Double lng;

    @Positive
    private Double distance;

    private List<String> categories;

    private String searchReg;

    private PlaceStatus status;

    private String time;
}
