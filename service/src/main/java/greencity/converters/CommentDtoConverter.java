package greencity.converters;

import greencity.dto.comment.CommentDto;
import greencity.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentDtoConverter {
    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdDate(comment.getCreatedDate())
                .userName(comment.getUser().getName())
                .parentCommentId(Optional.ofNullable(comment.getParentComment())
                        .map(Comment::getId)
                        .orElse(null))
                .build();
    }
}
