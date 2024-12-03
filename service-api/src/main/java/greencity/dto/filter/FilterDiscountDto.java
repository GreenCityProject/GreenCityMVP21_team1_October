package greencity.dto.filter;

import greencity.dto.specification.SpecificationNameDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDiscountDto {
    private SpecificationNameDto specification;

    @Min(0)
    @Max(100)
    private int discountMin;

    @Min(0)
    @Max(100)
    private int discountMax;
}
