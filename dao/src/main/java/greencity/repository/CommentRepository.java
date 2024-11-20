package greencity.repository;

import greencity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Find all comments for a specific parent comment.
     */
    List<Comment> findAllByParentCommentId(Long parentCommentId);

    /**
     * Find all comments related to the user.
     */
    List<Comment> findAllByUserId(Long userId);
}
