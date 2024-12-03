package greencity.mapping;

import greencity.dto.comment.CommentDto;
import greencity.dto.place.DiscountValueDto;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.specification.SpecificationNameDto;
import greencity.entity.Place;
import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlaceInfoDtoMapper extends AbstractConverter<Place, PlaceInfoDto> {

    private OpenHoursDtoMapper openHoursDtoMapper;
    private LocationDtoMapper locationDtoMapper;

    @Override
    public PlaceInfoDto convert(Place place) {
        return PlaceInfoDto.builder()
                .id(place.getId())
                .name(place.getName())
                .location(locationDtoMapper.convert(place.getLocation()))
                .rate(place.getRate())
                .openingHoursList(place.getOpenHoursList().stream()
                        .map(e -> openHoursDtoMapper.convert(e))
                        .toList())
                .comments(place.getComments().stream()
                        .map(e -> new CommentDto(e.getText()))
                        .toList())
                .discountValues(place.getDiscountValues().stream()
                        .map(e -> DiscountValueDto.builder()
                                .specification(new SpecificationNameDto("specification name"))
                                .value(e.getValue())
                                .build())
                        .toList())
                .build();
    }
}
