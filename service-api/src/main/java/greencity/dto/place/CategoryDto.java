package greencity.dto.place;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String name;
    private String nameUa;
    private Long parentCategoryId;
}
