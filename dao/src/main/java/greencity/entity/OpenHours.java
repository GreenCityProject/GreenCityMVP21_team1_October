package greencity.entity;

import greencity.enums.WeekDay;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Time;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "open_hours_list")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"breakTime", "place"})
@Builder
public class OpenHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @NotNull(message = "Day of week can't be null!")
    private WeekDay weekDay;

    @NotNull(message = "Open time can't be null!")
    private Time openTime;

    @NotNull(message = "Close time can't be null!")
    private Time closeTime;

    @OneToOne(mappedBy = "openHours", cascade = CascadeType.ALL, orphanRemoval = true)
    private BreakTime breakTime;

    @ManyToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;
}
