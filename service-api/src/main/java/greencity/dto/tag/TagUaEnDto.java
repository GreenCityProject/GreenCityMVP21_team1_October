package greencity.dto.tag;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagUaEnDto {
    private long id;
    private String nameUa;
    private String nameEn;
}
