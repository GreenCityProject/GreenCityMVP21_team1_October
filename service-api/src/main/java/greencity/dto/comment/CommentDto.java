package greencity.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private String userName;
    private Long parentCommentId;
    private LocalDateTime createdDate;
}
