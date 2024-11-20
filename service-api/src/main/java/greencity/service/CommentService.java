package greencity.service;

import greencity.dto.comment.AddCommentDto;
import greencity.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {
    /**
     * Add a new comment.
     *
     * @param addCommentDto DTO with data to create a comment.
     * @param userId        The ID of the user creating the comment.
     * @return Saved Comment.
     */
    CommentDto addComment(AddCommentDto addCommentDto, Long userId);

    /**
     * Get all comments for the parent comment.
     *
     * @param parentCommentId The ID of the parent comment.
     * @return List of comments.
     */
    List<CommentDto> getReplies(Long parentCommentId);

    /**
     * Get all user comments.
     *
     * @param userId User ID.
     * @return List of comments.
     */
    List<CommentDto> getCommentsByUser(Long userId);
}
