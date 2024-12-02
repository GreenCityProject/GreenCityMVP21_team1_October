package greencity.mapping;

import greencity.dto.eventcomment.AddEventCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentAuthorDto;
import greencity.dto.user.UserVO;
import org.modelmapper.AbstractConverter;

public class UserVOAddEventCommentDtoRequestMapper extends AbstractConverter<UserVO, AddEventCommentDtoResponse> {
    @Override
    protected AddEventCommentDtoResponse convert(UserVO userVO) {
        return AddEventCommentDtoResponse.builder()
                .author(EventCommentAuthorDto.builder()
                        .id(userVO.getId())
                        .name(userVO.getName())
                        .userProfilePicturePath(userVO.getProfilePicturePath())
                        .build())
                .build();
    }
}
