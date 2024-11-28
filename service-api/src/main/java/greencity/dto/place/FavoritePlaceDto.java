package greencity.dto.place;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FavoritePlaceDto {
    private String name;
    private long placeId;
}
