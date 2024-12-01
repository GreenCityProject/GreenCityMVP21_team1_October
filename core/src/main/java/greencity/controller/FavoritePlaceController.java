package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.place.FavoritePlaceDto;
import greencity.dto.place.PlaceByBoundsDto;
import greencity.dto.user.UserVO;
import greencity.service.FavoritePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite_place")
@AllArgsConstructor
public class FavoritePlaceController {
    private FavoritePlaceService favoritePlaceService;

    @Operation(description = "Delete favorite place by user email")
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

    @Operation(description = "Get favorite place with Coordinate by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/favorite/{placeId}")
    public ResponseEntity<PlaceByBoundsDto> getFavoritePlaceWithCoordinateUsingGET(
            @PathVariable("placeId") long placeId,
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity.ok(favoritePlaceService.getFavoriteByUserAndPlaceId(userVO, placeId));
    }

    @Operation(description = "Get list favorite places by user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED)
    })
    @GetMapping
    public ResponseEntity<List<PlaceByBoundsDto>> getFavoritePlaceWithCoordinateUsingGET(
            @Parameter(hidden = true) @CurrentUser UserVO userVO
    ) {
        return ResponseEntity.ok(favoritePlaceService.getAllFavoritesByUser(userVO));
    }

    @Operation(description = "Update user favorite place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PutMapping
    public ResponseEntity<FavoritePlaceDto> updateUsingPUT3(
            @RequestBody FavoritePlaceDto favoritePlaceDto,
            @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        favoritePlaceService.updateFavoritePlace(favoritePlaceDto, userVO);
        return ResponseEntity.ok(favoritePlaceDto);
    }
}
