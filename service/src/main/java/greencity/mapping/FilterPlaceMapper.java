package greencity.mapping;

import greencity.dto.filter.FilterDiscountDto;
import greencity.dto.filter.FilterDistanceDto;
import greencity.dto.place.FilterPlaceDto;
import greencity.dto.place.MapBoundsDto;
import greencity.dto.specification.SpecificationNameDto;
import greencity.filters.FilterPlace;
import java.util.Optional;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class FilterPlaceMapper extends AbstractConverter<FilterPlaceDto, FilterPlace> {
    @Override
    protected FilterPlace convert(FilterPlaceDto source) {
        FilterDistanceDto filterDistanceDto =
            Optional.ofNullable(source.getDistanceFromUserDto()).orElse(new FilterDistanceDto());
        FilterDiscountDto filterDiscount =
            Optional.ofNullable(source.getDiscountDto()).orElseGet(() -> {
                FilterDiscountDto filterDiscountDto = new FilterDiscountDto();
                filterDiscountDto.setSpecification(new SpecificationNameDto());
                return filterDiscountDto;
            });
        MapBoundsDto mapBounds =
            Optional.ofNullable(source.getMapBoundsDto()).orElse(new MapBoundsDto());

        return FilterPlace.builder()
            .time(source.getTime())
            .status(source.getStatus())
            .searchReg(source.getSearchReg())
            .categories(source.getCategories())
            .distance(filterDistanceDto.getDistance())
            .lng(filterDistanceDto.getLng())
            .lat(filterDistanceDto.getLat())
            .discountMax(filterDiscount.getDiscountMax())
            .discountMin(filterDiscount.getDiscountMin())
            .specificationName(filterDiscount.getSpecification().getName())
            .southWestLng(mapBounds.getSouthWestLng())
            .northEastLng(mapBounds.getNorthEastLng())
            .southWestLat(mapBounds.getSouthWestLat())
            .northEastLat(mapBounds.getNorthEastLat())
            .build();
    }
}
