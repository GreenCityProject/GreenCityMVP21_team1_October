package greencity.repository;

import greencity.entity.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDayRepository extends JpaRepository<EventDay, Long> {
}
