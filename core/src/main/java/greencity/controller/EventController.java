package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.annotations.EventValidation;
import greencity.constant.ErrorMessage;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventResponseDto;
import greencity.dto.event.MyEventsResponseDto;
import greencity.dto.user.UserVO;
import greencity.enums.EventStatus;
import greencity.enums.EventType;
import greencity.exception.exceptions.WrongIdException;
import greencity.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

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
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @PathVariable Long eventId,
            @RequestPart(required = false) @Nullable MultipartFile[] file) {

        if (!eventId.equals(requestDto.getId())) {
            throw new WrongIdException(ErrorMessage.EVENT_ID_IN_PATH_PARAM_AND_ENTITY_NOT_EQUAL);
        }

        return ResponseEntity.ok().body(eventService.update(requestDto, userVO.getName(), file));
    }

    /**
     * Method for deleting an event.
     * This endpoint allows an Admin or the Organizer of the event to delete it.
     *
     * @param eventId ID of the event to be deleted.
     * @param userVO  the currently authenticated user.
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
            @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        eventService.deleteEvent(eventId, userVO.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Method for getting all events related to the authenticated user.
     * This includes both events created by the user and events the user has joined.
     */
    @Operation(summary = "Get all events created or joined by the authenticated user. Supports optional filters by event type and pagination details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = PageableAdvancedDto.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST)))
    })
    @GetMapping
    public ResponseEntity<PageableAdvancedDto<EventResponseDto>> getUserEvents(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @RequestParam(required = false) EventType eventType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Double userLatitude,
            @RequestParam(required = false) Double userLongitude) {
        Pageable pageable = PageRequest.of(page, size);

        PageableAdvancedDto<EventResponseDto> userEvents = eventService.getUserEvents(
                userVO.getId(), eventType, userLatitude, userLongitude, pageable);

        return ResponseEntity.ok(userEvents);
    }

    /**
     * Method for getting all events created by the authenticated user.
     */
    @Operation(summary = "Get all events created by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = MyEventsResponseDto.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED)))
    })
    @GetMapping("/createdEvents")
    public ResponseEntity<MyEventsResponseDto> getCreatedEvents(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @RequestParam EventStatus status) {
        List<EventResponseDto> createdEvents = eventService.getCreatedEventsByUser(userVO.getId(), status);
        List<EventResponseDto> attendingEvents = eventService.getAttendingEventsByUser(userVO.getId(), status);

        MyEventsResponseDto response = new MyEventsResponseDto(createdEvents, attendingEvents);

        return ResponseEntity.ok(response);
    }

    /**
     * Method for getting all related events for the user.
     */
    @Operation(summary = "Get all related events for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(schema = @Schema(implementation = EventResponseDto.class))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED)))
    })
    @GetMapping("/relatedEvents")
    public ResponseEntity<List<EventResponseDto>> getUserRelatedEvents(
            @RequestParam("status") EventStatus status,
            @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        List<EventResponseDto> relatedEvents = eventService.getAttendingEventsByUser(userVO.getId(), status);

        return ResponseEntity.ok(relatedEvents);
    }
}