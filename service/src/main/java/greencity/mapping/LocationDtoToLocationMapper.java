package greencity.mapping;

import greencity.dto.location.LocationDto;
import greencity.entity.Location;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationDtoToLocationMapper extends AbstractConverter<LocationDto, Location> {

    @Override
    protected Location convert(LocationDto source) {
        return Location.builder()
                .id(source.getId())
                .lat(source.getLat())
                .lng(source.getLng())
                .build();
    }
}
