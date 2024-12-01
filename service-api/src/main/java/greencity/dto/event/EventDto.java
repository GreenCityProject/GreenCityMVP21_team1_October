package greencity.dto.event;

import lombok.Data;

@Data
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Boolean isOpen;
    private String organizerName;
}
