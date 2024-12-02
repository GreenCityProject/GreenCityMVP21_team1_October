package greencity.dto.place;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationAddressAndGeoDto {
    private String address;
    private Double lat;
    private Double ing;
}
