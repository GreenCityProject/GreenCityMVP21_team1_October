package greencity.service;

import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.event.EventVO;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.AddEventCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentVO;
import greencity.dto.user.UserVO;

public interface EventCommentService {
    /**
     * Method to save {@link EventCommentVO}.
     *
     * @param eventId                   id of {@link EventVO} to which we save
     *                                  comment.
     * @param addEventCommentDtoRequest dto with {@link EventCommentVO} text,
     *                                  parentCommentId.
     * @param user                      {@link UserVO} that saves the comment.
     * @return {@link AddEventCommentDtoResponse} instance.
     */
    AddEventCommentDtoResponse save(Long eventId, AddEventCommentDtoRequest addEventCommentDtoRequest,
                                    UserVO user);

    /**
     * Method to update {@link EcoNewsCommentVO}.
     *
     * @param newText   updated text of {@link EventCommentVO} to which we update
     *                  comment.
     * @param commentId id of eventId {@link EventCommentVO}
     * @param user      {@link UserVO} that saves the comment.
     */
    void update(String newText, long commentId, UserVO user);

    /**
     * Method to delete {@link EventCommentVO}.
     *
     * @param commentId id of the {@link EventCommentVO} to be deleted.
     * @param user      {@link UserVO} who requests the deletion.
     */
    void delete(long commentId, UserVO user);

    /**
     * Method to find {@link EventCommentVO} by ID.
     *
     * @param commentId id of the {@link EventCommentVO} to be found.
     * @return {@link EventCommentVO} found EventVO.
     */
    EventCommentVO findById(long commentId);

    /**
     * Method to delete {@link EventCommentVO}.
     *
     * @param eventId id of the {@link EventCommentVO} to be liked.
     * @param user      {@link UserVO} who requests the comment like.
     * @return {@link AddEventCommentDtoResponse} liked comment.
     */
    AddEventCommentDtoResponse likeEventComment(Long eventId, UserVO user);
}
