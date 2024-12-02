package greencity.dto.place;

import greencity.dto.specification.SpecificationNameDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DiscountValueDto {
    private SpecificationNameDto specification;
    private Integer value;
}
