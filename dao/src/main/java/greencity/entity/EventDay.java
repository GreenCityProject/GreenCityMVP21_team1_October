package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event_days")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@ToString
public class EventDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime eventStartTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime eventEndTime;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "is_online")
    private Boolean isOnline;

    @Column(name = "online_link")
    private String onlineLink;
}
