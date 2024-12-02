package greencity.dto.place;

import greencity.dto.comment.CommentDto;
import greencity.dto.location.LocationDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PlaceInfoDto {
    private Long id;
    private String name;
    private LocationDto location;
    private Double rate;
    private List<OpenHoursDto> openingHoursList;
    private List<CommentDto> comments;
    private List<DiscountValueDto> discountValues;
}
