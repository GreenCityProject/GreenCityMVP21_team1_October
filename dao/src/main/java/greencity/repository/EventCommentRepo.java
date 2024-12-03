package greencity.repository;

import greencity.entity.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCommentRepo extends JpaRepository<EventComment, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO event_comments_users_liked VALUES (?1, ?2)")
    void likeComment(long commentId, long userId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM event_comments_users_liked WHERE event_comment_id = ?1 AND users_liked_id = ?2")
    void deleteComment(long commentId, long userId);

    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM event_comments_users_liked WHERE event_comment_id = ?1 AND users_liked_id = ?2)")
    boolean existsByCommentIdAndUserId(long commentId, long userId);
    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM event_comments_users_liked WHERE event_comment_id = ?1)")
    boolean existsByCommentId(long commentId);
}
