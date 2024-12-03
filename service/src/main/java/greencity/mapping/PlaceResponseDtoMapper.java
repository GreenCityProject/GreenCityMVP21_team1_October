package greencity.mapping;

import greencity.dto.place.PlaceResponseDto;
import greencity.entity.Place;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class PlaceResponseDtoMapper extends AbstractConverter<Place, PlaceResponseDto> {
    @Override
    protected PlaceResponseDto convert(Place source) {
        return null;
    }
}
