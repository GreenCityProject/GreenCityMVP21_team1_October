package greencity.filters;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class FilterPlaceCategory {
    private Long id;
    private String name;
    private String nameUa;
}
