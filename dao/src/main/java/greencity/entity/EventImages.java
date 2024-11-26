package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String link;

    @ManyToOne
    private Event event;
}
