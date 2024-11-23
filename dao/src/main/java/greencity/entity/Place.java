package greencity.entity;

import greencity.enums.PlaceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "places")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 30, message = "Place name should be from 1 to 30 characters long")
    @Column(unique = true)
    private String name;

    @NotNull
    private Double rate;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PlaceStatus status;

    @ManyToOne
    @NotNull(message = "Place location can't be null!")
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Location location;

    @ManyToOne
    @NotNull(message = "Place category can't be null!")
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Category category;

    @ManyToOne
    @NotNull(message = "Place author can't be null!")
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private User author;
}
