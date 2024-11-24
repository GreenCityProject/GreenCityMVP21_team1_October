package greencity.service;

import greencity.dto.event.EventResponseDto;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventVO;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface EventService {
    /**
     * Method for updating user event {@link EventResponseDto}.
     *
     * @param requestDto - event update.
     * @param email      - user that edits the event.
     * @param files      - new event images.
     * @return EventResponseDto.
     */
    EventResponseDto update(EventDetailsUpdate requestDto, String email, MultipartFile[] files);

    /**
     * Method for deleting an event.
     *
     * <p>This method deletes the specified event if the user has the required permissions
     * (either as the event organizer or as an admin). It removes all related data
     * associated with the event.
     *
     * @param eventId the ID of the event to be deleted.
     * @param userId the ID of the user attempting to delete the event.
     * @throws UserHasNoPermissionToAccessException if the user does not have permission
     *         to delete the event.
     * @throws NotFoundException if the event with the specified ID is not found.
     */
    void deleteEvent(Long eventId, Long userId);

    /**
     * Method for getting events created by a user.
     *
     * @param userId the ID of the user.
     * @return list of {@link EventResponseDto}.
     */
    List<EventResponseDto> getCreatedEventsByUser(Long userId);

    /**
     * Method for getting events where a user is an attender.
     *
     * @param userId the ID of the user.
     * @return list of {@link EventResponseDto}.
     */
    List<EventResponseDto> getAttendingEventsByUser(Long userId);

    EventVO findById(long eventId);
}
