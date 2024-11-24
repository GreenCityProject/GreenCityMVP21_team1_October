package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.place.PlaceUpdateDto;
import greencity.exception.exceptions.NotFoundException;
import greencity.mapping.PlaceInfoDtoMapper;
import greencity.mapping.PlaceUpdateDtoMapper;
import greencity.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;

    private PlaceInfoDtoMapper placeInfoDtoMapper;
    private PlaceUpdateDtoMapper placeUpdateDtoMapper;

    @Override
    public PlaceInfoDto getPlaceInfo(Long id) {
        return placeInfoDtoMapper.convert(placeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PLACE_NOT_FOUND_BY_ID + id)));
    }

    @Override
    public PlaceUpdateDto getPlace(Long id) {
        return placeUpdateDtoMapper.convert(placeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PLACE_NOT_FOUND_BY_ID + id)));
    }
}
