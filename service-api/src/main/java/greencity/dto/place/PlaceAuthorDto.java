package greencity.dto.place;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceAuthorDto {
    private String email;
    private Long id;
    private String name;
}
