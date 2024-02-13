package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileServiceController {
    private final FileService fileService;

    /**
     * Constructor.
     */
    @Autowired
    public FileServiceController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Method for uploading an image.
     *
     * @param image image to save.
     * @return url of the saved image.
     */
    @Operation(summary = "Upload an image.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED)
    })
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestPart @NotEmpty MultipartFile image) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.upload(image));
    }
}
