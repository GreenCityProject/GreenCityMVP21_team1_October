package greencity.dto.comment;

import greencity.constant.ServiceValidationConstants;
import greencity.dto.rate.EstimateAddDto;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentDto {
    @NotBlank(message = "Comment text cannot be blank.")
    @Length(min = ServiceValidationConstants.COMMENT_MIN_LENGTH,
            max = ServiceValidationConstants.COMMENT_MAX_LENGTH,
            message = "Comment must be between {min} and {max} characters.")
    private String text;
    @Valid
    private EstimateAddDto estimate;
    @PositiveOrZero(message = "Parent comment ID must be a positive number.")
    private Long parentCommentId;
}
