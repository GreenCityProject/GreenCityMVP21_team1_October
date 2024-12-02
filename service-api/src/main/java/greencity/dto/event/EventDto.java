package greencity.dto.event;

import greencity.dto.tag.TagUaEnDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isFavorite;
    private Boolean isSubscribed;
    private Boolean open;
    private List<TagUaEnDto> tags;
    private String titleImage;
    private List<String> additionalImages;
    private List<EventDayResponseDto> dates;
    private String creationDate;
}
