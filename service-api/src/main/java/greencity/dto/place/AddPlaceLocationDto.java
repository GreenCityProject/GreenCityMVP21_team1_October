package greencity.dto.place;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPlaceLocationDto {
    private String address;
    private String addressEng;
    private Double lat;
    private Double lng;
}
