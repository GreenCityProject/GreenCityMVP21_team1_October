package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.user.UserVO;
import greencity.service.FavoritePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite_place")
@AllArgsConstructor
public class FavoritePlaceController {
    private FavoritePlaceService favoritePlaceService;

    @Operation(description = "Delete favorite place by userEmail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{placeId}")
    public ResponseEntity<Long> deleteByUserEmailAndPlaceIdUsingDELETE(
            @Parameter(hidden = true) @CurrentUser UserVO userVO, @PathVariable("placeId") long placeId
    ) {
        return ResponseEntity.ok(favoritePlaceService.deleteFavoritePlaceById(placeId, userVO));
    }
}
