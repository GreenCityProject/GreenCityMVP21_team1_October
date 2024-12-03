package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.eventcomment.AddEventCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddEventCommentDtoResponseMapperTest {
    private AddEventCommentDtoResponseMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new AddEventCommentDtoResponseMapper();
    }

    @Test
    public void mapperTest() {
        EventCommentVO eventCommentVO = ModelUtils.getEventCommentVO();
        AddEventCommentDtoResponse response = mapper.convert(eventCommentVO);
        assertEquals(response.getId(), eventCommentVO.getId());
        assertEquals(response.getComment(), eventCommentVO.getComment());
        assertEquals(response.getModifiedDate(), eventCommentVO.getModifiedDate());
        assertEquals(response.getAuthor().getName(), eventCommentVO.getUser().getName());
        assertEquals(response.getAuthor().getId(), eventCommentVO.getUser().getId());
        assertEquals(response.getAuthor().getName(), eventCommentVO.getUser().getName());
        assertEquals(response.getAuthor().getUserProfilePicturePath(), eventCommentVO.getUser().getProfilePicturePath());
    }
}
