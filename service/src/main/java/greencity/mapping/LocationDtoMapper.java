package greencity.mapping;

import greencity.dto.location.LocationDto;
import greencity.entity.Location;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper extends AbstractConverter<Location, LocationDto> {

    @Override
    protected LocationDto convert(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .address(location.getAddress())
                .lat(location.getLat())
                .lng(location.getLng())
                .build();
    }
}
