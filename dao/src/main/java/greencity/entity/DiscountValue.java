package greencity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "discount_values")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class DiscountValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Discount value can't be null!")
    @Min(value = 0, message = "Discount value can't be negative!")
    @Max(value = 100, message = "Discount value can't be more than 100 percent!")
    private Integer value;

    @ManyToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    Place place;
}
