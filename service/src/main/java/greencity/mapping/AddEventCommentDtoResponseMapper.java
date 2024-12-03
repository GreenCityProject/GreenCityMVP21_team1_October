package greencity.mapping;

import greencity.dto.eventcomment.AddEventCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentAuthorDto;
import greencity.dto.eventcomment.EventCommentVO;
import org.modelmapper.AbstractConverter;

public class AddEventCommentDtoResponseMapper extends AbstractConverter<EventCommentVO, AddEventCommentDtoResponse> {
    @Override
    protected AddEventCommentDtoResponse convert(EventCommentVO eventCommentVO) {
        return AddEventCommentDtoResponse.builder()
                .id(eventCommentVO.getId())
                .comment(eventCommentVO.getComment())
                .modifiedDate(eventCommentVO.getModifiedDate())
                .author(EventCommentAuthorDto.builder()
                        .id(eventCommentVO.getUser().getId())
                        .name(eventCommentVO.getUser().getName())
                        .userProfilePicturePath(eventCommentVO.getUser().getProfilePicturePath())
                        .build())
                .build();
    }
}
