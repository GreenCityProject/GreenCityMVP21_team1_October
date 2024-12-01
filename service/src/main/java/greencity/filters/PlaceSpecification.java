package greencity.filters;

import greencity.entity.*;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

public class PlaceSpecification implements Specification<Place> {
    private final transient FilterPlace filter;

    public PlaceSpecification(FilterPlace filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Place> root, @NotNull CriteriaQuery<?> query,
                                 @NotNull CriteriaBuilder cb) {
        Predicate curPredicate = cb.conjunction();

        if (StringUtils.isNotEmpty(filter.getSearchReg())) {
            curPredicate = cb.and(curPredicate, cb.like(root.get(Place_.NAME), "%" + filter.getSearchReg() + "%"));
        }

        if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            curPredicate =
                cb.and(curPredicate, root.join(Place_.category).get(Category_.NAME).in(filter.getCategories()));
        }

        if (filter.getStatus() != null) {
            curPredicate = cb.and(curPredicate, cb.equal(root.get(Place_.STATUS), filter.getStatus()));
        }

        if (filter.getDiscountMax() > 0 || filter.getDiscountMin() > 0) {
            Join<Place, DiscountValue> discount = root.join(Place_.discountValues);

            if (filter.getDiscountMax() > 0) {
                curPredicate =
                    cb.and(curPredicate,
                        cb.lessThanOrEqualTo(discount.get(DiscountValue_.VALUE), filter.getDiscountMax()));
            }
            if (filter.getDiscountMin() > 0) {
                curPredicate =
                    cb.and(curPredicate,
                        cb.greaterThanOrEqualTo(discount.get(DiscountValue_.VALUE), filter.getDiscountMin()));
            }
        }
        if (
            filter.getNorthEastLat() != null
                && !filter.getNorthEastLat().isNaN()
                && filter.getSouthWestLat() != null
                && !filter.getSouthWestLat().isNaN()
                && filter.getNorthEastLng() != null
                && !filter.getNorthEastLng().isNaN()
                && filter.getSouthWestLng() != null
                && !filter.getSouthWestLng().isNaN()
        ) {
            Join<Place, Location> location = root.join(Place_.location);

            curPredicate = cb.and(curPredicate,
                cb.and(
                    cb.between(
                        location.get(Location_.lat), filter.getSouthWestLat(), filter.getNorthEastLat()
                    ),
                    cb.between(
                        location.get(Location_.lng), filter.getSouthWestLng(), filter.getNorthEastLng()
                    ))
            );
        }

        if (
            filter.getLng() != null
                && !filter.getLng().isNaN()
                && filter.getLat() != null
                && !filter.getLat().isNaN()
                && filter.getDistance() != null
                && !filter.getDistance().isNaN()
        ) {
            Join<Place, Location> location = root.join(Place_.location);

            // and SQRT(POW({user lat} - l.lat, 2) + POW({user lon} - l.lon, 2)) < {distance}
            curPredicate = cb.and(
                curPredicate,
                cb.lessThanOrEqualTo(
                    cb.sqrt(cb.sum(
                        cb.power(cb.diff(filter.getLng(), location.get(Location_.lng)), 2),
                        cb.power(cb.diff(filter.getLat(), location.get(Location_.lat)), 2)
                    )),
                    filter.getDistance())
            );
        }
        return curPredicate;
    }
}
