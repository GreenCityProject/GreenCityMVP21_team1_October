package greencity.controller;

import greencity.annotations.EventValidation;
import greencity.constant.ErrorMessage;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventRequestDto;
import greencity.dto.event.EventResponseDto;
import greencity.dto.event.PageableAdvancedDtoOfEventDto;
import greencity.dto.user.UserVO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    /**
     * Method for retrieving all events with pagination.
     *
     * @param page the page index (default is 0).
     * @param size the number of records per page (default is 5).
     * @return PageableAdvancedDtoOfEventDto.
     */
    @Operation(summary = "Get all events with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = PageableAdvancedDtoOfEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<PageableAdvancedDtoOfEventDto> getAllEvents(
            @Parameter(description = "Page index you want to retrieve [0..N]. If page index is less than 0, default value is used (0).")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page [1..100]. If size is less than 1 or not specified, default value is used (5).")
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String eventTime) {

        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(page, size);

        PageableAdvancedDtoOfEventDto result;

        if (eventTime != null) {
            result = eventService.getFilteredEvents(pageable, eventTime);
        } else {
            result = eventService.getAllEvents(pageable);
        }

        return ResponseEntity.ok(result);
    }

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

    @Operation(summary = "Create event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
                    content = @Content(schema = @Schema(implementation = EventResponseDto.class))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN,
                    content = @Content(examples = @ExampleObject(HttpStatuses.FORBIDDEN)))
    })
    @PostMapping
    public ResponseEntity<EventResponseDto> save(
            @Parameter(required = true) @EventValidation @RequestPart EventRequestDto eventRequestDto,
                                                 @Parameter(hidden = true) Principal principal,
                                                 @RequestPart(required = false) @Nullable MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(eventRequestDto,
                principal.getName(),
                files));
    }
}
