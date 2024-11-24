package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Time;

@Entity
@Table(name = "break_times")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class BreakTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Break start time can't be null!")
    private Time startTime;

    @NotNull(message = "Break end time can't be null!")
    private Time endTime;

    @OneToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OpenHours openHours;
}
