package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.annotations.EventValidation;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventRequestDto;
import greencity.dto.event.EventResponseDto;
import greencity.dto.event.PageableAdvancedDtoOfEventDto;
import greencity.dto.user.UserVO;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
        @Parameter(required = true) @Valid @RequestPart EventDetailsUpdate requestDto,
        @Parameter(hidden = true) @CurrentUser UserVO userVO,
        @PathVariable Long eventId,
        @RequestPart(required = false) @Nullable MultipartFile[] file) {

        return ResponseEntity.ok().body(eventService.update(requestDto, eventId, userVO.getEmail(), file));
    }


    /**
     * Method for deleting an event.
     * This endpoint allows an Admin or the Organizer of the event to delete it.
     *
     * @param eventId   ID of the event to be deleted.
     * @param userVO the currently authenticated user.
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
        UserVO currentUser = userService.findByEmail(userVO.getEmail());
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDto> save(
            @Parameter(required = true) @EventValidation @RequestPart EventRequestDto eventRequestDto,
                                                 @Parameter(hidden = true) @CurrentUser UserVO userVO,
                                                 @RequestPart(required = false) @Nullable MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(eventRequestDto,
                userVO.getEmail(),
                files));
    }

    /**
     * Method for retrieving events with optional filters and pagination.
     *
     * <p>If no filter parameters are provided, this method retrieves all events with pagination.
     * Otherwise, it applies the specified filters to fetch a filtered list of events.</p>
     *
     * @param page the page index to retrieve (default is 0).
     * @param size the number of records per page (default is 5).
     * @param eventTime the filter for event time (optional). Valid values are "Upcoming" or "Passed".
     * @param location the filter for event location (optional). Use "Online" for online events or a city name.
     * @param tags a list of tags to filter by (optional).
     * @param status the filter for event status (optional). Valid values are "Open", "Closed", "Joined", "Created".
     *               For unauthenticated users, only "Open" and "Closed" are allowed.
     * @param currentUser the currently authenticated user (optional).
     * @return a paginated list of events, either filtered or unfiltered.
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
            @RequestParam(required = false) String eventTime,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String status,
            @Parameter(hidden = true) @CurrentUser UserVO currentUser) {

        Pageable pageable = PageRequest.of(page, size);

        PageableAdvancedDtoOfEventDto result;
        if (eventTime == null && location == null && (tags == null || tags.isEmpty()) && status == null) {
            result = eventService.getAllEvents(pageable);
        } else {
            result = eventService.getFilteredEvents(pageable, eventTime, location, tags, status, currentUser);
        }
        return ResponseEntity.ok(result);
    }
}
