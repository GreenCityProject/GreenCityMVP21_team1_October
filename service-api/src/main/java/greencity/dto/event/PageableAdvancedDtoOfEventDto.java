package greencity.dto.event;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableAdvancedDtoOfEventDto {
    private Integer currentPage;
    private Boolean first;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean last;
    private Integer number;
    private List<EventDto> page;
    private Long totalElements;
    private Integer totalPages;
}
