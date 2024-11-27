package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.place.*;
import greencity.dto.user.UserVO;
import greencity.enums.PlaceStatus;
import greencity.filters.FilterPlaceCategory;
import greencity.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/place")
@AllArgsConstructor
public class PlaceController {
    private PlaceService placeService;

    @Operation(summary = "Get info about place")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/info/{id}")
    public ResponseEntity<PlaceInfoDto> getPlaceInfo(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceInfo(id));
    }

    @Operation(summary = "Get place by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/about/{id}")
    public ResponseEntity<PlaceUpdateDto> getPlace(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlace(id));
    }

    @Operation(summary = "Get places by status (APPROVED, PROPOSED, DECLINED, DELETED).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @ApiPageable
    @GetMapping("/{status}")
    public ResponseEntity<PageableDto<PlaceInfoDto>> getPlaces(@PathVariable PlaceStatus status,
                                                               @Parameter(hidden = true) Pageable page) {
        return ResponseEntity.ok(placeService.getPlaces(status, page));
    }


    @Operation(summary = "Get array of available place statuses.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.ok(placeService.getStatuses());
    }

    @Operation(summary = "Return all place categories to filter.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK)
    })
    @GetMapping("/v2/filteredPlacesCategories")
    public ResponseEntity<List<FilterPlaceCategory>> getFilteredPlacesCategories() {
        return ResponseEntity.ok(placeService.getFilteredPlacesCategories());
    }

    @Operation(summary = "Bulk delete places.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping
    public ResponseEntity<Long> bulkDeletePlaces(
        @Parameter(description = "Ids to delete separated by comma") @RequestParam String ids) {
        return ResponseEntity.ok(placeService.bulkDeletePlaces(ids));
    }

    @Operation(summary = "Bulk update place status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PatchMapping("/statuses")
    public ResponseEntity<List<UpdatePlaceStatusDto>> bulkUpdatePlaceStatus(@RequestBody BulkUpdatePlaceStatusDto dto) {
        return ResponseEntity.ok(placeService.bulkUpdatePlaceStatus(dto));
    }

    @Operation(summary = "Create new place from UI")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/v2/save")
    public ResponseEntity<PlaceResponseDto> saveEcoPlaceFromUiUsing(
        @RequestBody AddPlaceDto placeDto, @CurrentUser UserVO userVO) {
        return ResponseEntity.ok(placeService.save(placeDto, userVO));
    }

    @Operation(summary = "Return a list places filtered by values contained in the incoming FilterPlaceDto object")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/filter")
    public ResponseEntity<List<FilterPlaceResponseDto>> getFilteredPlacesUsing(
        @RequestBody FilterPlaceDto filterPlaceDto,
        @CurrentUser UserVO userVO
    ) {
        return ResponseEntity.ok(placeService.getFilteredPlaces(filterPlaceDto, userVO));
    }

    @Operation(summary = "Return a list places filtered by values contained in the incoming FilterPlaceDto object")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/filter/predicate")
    public ResponseEntity<PageableAdvancedDto<FilterPlaceResponseDto>> filterPlaceBySearchPredicateUsing(
        @RequestBody FilterPlaceDto filterPlaceDto,
        @CurrentUser UserVO userVO,
        @Parameter(hidden = true) Pageable page
    ) {
        return ResponseEntity.ok(placeService.getFilteredPlaces(filterPlaceDto, userVO, page));
    }
}
