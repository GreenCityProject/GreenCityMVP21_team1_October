package greencity.controller;

import greencity.dto.comment.AddCommentDto;
import greencity.dto.comment.CommentDto;
import greencity.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Validated
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Adding a new comment.
     *
     * @param addCommentDto DTO with the text of the comment and the ID of the parent comment.
     * @param principal     Authorized User.
     * @return Created Comment.
     */
    @PostMapping
    @Operation(summary = "Add a new comment to an event.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddCommentDto.class),
                            examples = @ExampleObject(value = """
                            {
                                "text": "This is a comment",
                                "parentCommentId": 1,
                                "eventId": 2
                            }
                            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully.",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "Parent comment not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CommentDto> addComment(@RequestBody @Validated AddCommentDto addCommentDto, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        CommentDto savedComment = commentService.addComment(addCommentDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    /**
     * Receiving all responses to the comment.
     *
     * @param parentCommentId The ID of the parent comment.
     * @return List of child comments.
     */
    @GetMapping("/{parentCommentId}/replies")
    @Operation(summary = "Get all replies for a specific comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Replies retrieved successfully.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))),
            @ApiResponse(responseCode = "404", description = "Parent comment not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<CommentDto>> getReplies(@PathVariable Long parentCommentId) {
        List<CommentDto> replies = commentService.getReplies(parentCommentId);
        return ResponseEntity.ok(replies);
    }

    /**
     * Retrieve all the comments of a particular user.
     *
     * @param principal Authorized User.
     * @return List of user comments.
     */
    @GetMapping("/user")
    @Operation(summary = "Get all comments by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User comments retrieved successfully.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<List<CommentDto>> getCommentsByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<CommentDto> userComments = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(userComments);
    }
}
