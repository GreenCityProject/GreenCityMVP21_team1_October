package greencity.controller;

import greencity.annotations.EventValidation;
import greencity.constant.ErrorMessage;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventResponseDto;
import greencity.dto.event.MyEventsResponseDto;
import greencity.dto.user.UserVO;
import greencity.enums.EventStatus;
import greencity.exception.exceptions.WrongIdException;
import greencity.service.EventService;
import greencity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    /**
     * Method for updating event
     *
     * @return {@link EventResponseDto}
     * @author Dmytro Kaplun
     */
    @Operation(summary = "Update event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventResponseDto.class))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN,
                    content = @Content(examples = @ExampleObject(HttpStatuses.FORBIDDEN))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @PutMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDto> update(
        @Parameter(required = true) @EventValidation @RequestPart EventDetailsUpdate requestDto,
        @Parameter(hidden = true) Principal principal,
        @PathVariable Long eventId,
        @RequestPart(required = false) @Nullable MultipartFile[] file) {

        if (!eventId.equals(requestDto.getId())) {
            throw new WrongIdException(ErrorMessage.EVENT_ID_IN_PATH_PARAM_AND_ENTITY_NOT_EQUAL);
        }

        return ResponseEntity.ok().body(eventService.update(requestDto, principal.getName(), file));
    }

    /**
     * Method for deleting an event.
     * This endpoint allows an Admin or the Organizer of the event to delete it.
     *
     * @param eventId   ID of the event to be deleted.
     * @param principal the currently authenticated user.
     * @return {@link ResponseEntity<Void>}
     * @author Belchuk Stanislav
     */
    @Operation(summary = "Delete an event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN,
                    content = @Content(examples = @ExampleObject(HttpStatuses.FORBIDDEN))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @Parameter(hidden = true) Principal principal) {
        UserVO currentUser = userService.findByEmail(principal.getName());
        Long userId = currentUser.getId();
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.ok().build();
    }


    /**
     * Method for getting all events related to the authenticated user.
     *
     * @param principal the currently authenticated user.
     * @return {@link ResponseEntity<MyEventsResponseDto>}
     */
    @Operation(summary = "Get all events created by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = MyEventsResponseDto.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED)))
    })

    @GetMapping("/events/myEvents/createdEvents")
    public ResponseEntity<MyEventsResponseDto> getCreatedEvents(
            @Parameter(hidden = true) Principal principal,
            @RequestParam EventStatus status) {
        UserVO currentUser = userService.findByEmail(principal.getName());

        List<EventResponseDto> createdEvents = eventService.getCreatedEventsByUser(currentUser.getId(), status);
        List<EventResponseDto> attendingEvents = eventService.getAttendingEventsByUser(currentUser.getId(), status);

        MyEventsResponseDto response = new MyEventsResponseDto(createdEvents, attendingEvents);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all related events for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventResponseDto.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED)))
    })

    @GetMapping("/events/myEvents/relatedEvents")
    public List<EventResponseDto> getUserRelatedEvents(
            @RequestParam("status") EventStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Use the email/username to find the corresponding user in the database
        UserVO currentUser = userService.findByEmail(userDetails.getUsername());
        return eventService.getAttendingEventsByUser(currentUser.getId(), status);
    }

}
