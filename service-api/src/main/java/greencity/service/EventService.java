package greencity.service;

import greencity.dto.event.*;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import org.springframework.data.domain.Pageable;
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

    EventVO findById(long eventId);

    /**
     * Method for retrieving a paginated list of events.
     *
     * <p>This method retrieves events from the database in a paginated format. The
     * pagination is controlled using the {@link Pageable} parameter, which defines
     * the page number and the number of records per page. It returns a DTO
     * containing the list of events and metadata about the pagination.
     *
     * @param pageable an object containing pagination parameters (page number and size).
     *                 - If the page number is less than 0, it will default to 0.
     *                 - If the page size is less than 1 or greater than 100, it will
     *                   default to a predefined value (usually 5).
     * @return {@link PageableAdvancedDtoOfEventDto} containing a list of {@link EventDto}
     *         objects and pagination metadata.
     * @throws IllegalArgumentException if the pageable parameters are invalid.
     * @see PageableAdvancedDtoOfEventDto
     * @see EventDto
     * @see Pageable
     */
    PageableAdvancedDtoOfEventDto getAllEvents(Pageable pageable);

    PageableAdvancedDtoOfEventDto getFilteredEvents(Pageable pageable, String eventTime, String location, List<String> tags, String status, UserVO currentUser);

    EventResponseDto save(EventRequestDto eventRequestDto, String email, MultipartFile[] files);
}
