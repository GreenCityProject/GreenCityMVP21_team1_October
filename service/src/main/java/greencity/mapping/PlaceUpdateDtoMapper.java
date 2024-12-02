package greencity.mapping;

import greencity.dto.category.CategoryDto;
import greencity.dto.place.DiscountValueDto;
import greencity.dto.place.LocationAddressAndGeoForUpdateDto;
import greencity.dto.place.PlaceUpdateDto;
import greencity.dto.specification.SpecificationNameDto;
import greencity.entity.Place;
import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlaceUpdateDtoMapper extends AbstractConverter<Place, PlaceUpdateDto> {

    private OpeningHoursDtoMapper openingHoursDtoMapper;
    private LocationDtoMapper locationDtoMapper;

    @Override
    public PlaceUpdateDto convert(Place place) {
        return PlaceUpdateDto.builder()
                .id(place.getId())
                .name(place.getName())
                .location(locationDtoMapper.convert(place.getLocation()))
                .category(CategoryDto.builder()
                        .name(place.getCategory().getName())
                        .nameUa(place.getCategory().getNameUa())
                        .parentCategoryId(place.getCategory().getParentCategory() == null ? null
                                : place.getCategory().getParentCategory().getId())
                        .build())
                .openingHoursList(place.getOpenHoursList().stream()
                        .map(e -> openingHoursDtoMapper.convert(e))
                        .toList())
                .discountValues(place.getDiscountValues().stream()
                        .map(e -> DiscountValueDto.builder()
                                .specification(new SpecificationNameDto("specification name"))
                                .value(e.getValue())
                                .build())
                        .toList())
                .locationAddressAndGeoForUpdate(LocationAddressAndGeoForUpdateDto.builder()
                        .address(place.getLocation().getAddress())
                        .lat(place.getLocation().getLat())
                        .lng(place.getLocation().getLng())
                        .build())
                .build();
    }
}
