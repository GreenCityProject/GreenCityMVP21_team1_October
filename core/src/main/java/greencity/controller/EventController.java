package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.annotations.EventValidation;
import greencity.constant.HttpStatuses;
import greencity.dto.event.EventDetailsUpdate;
import greencity.dto.event.EventRequestDto;
import greencity.dto.event.EventResponseDto;
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

        return ResponseEntity.ok().body(eventService.update(requestDto, eventId, userVO.getName(), file));
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
        UserVO currentUser = userService.findByEmail(userVO.getName());
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
                                                 @Parameter(hidden = true) @CurrentUser UserVO userVO,
                                                 @RequestPart(required = false) @Nullable MultipartFile[] files) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(eventRequestDto,
                userVO.getName(),
                files));
    }
}
