package greencity.dto.place;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LocationAddressAndGeoForUpdateDto {
    private String address;
    private Double lat;
    private Double lng;
}
