package greencity.repository;

import greencity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long>, CustomEventRepository {

    /**
     * Method removes eventDays by event id
     *
     * @param id
     */
    @Modifying
    @Query(value = "DELETE FROM event_days WHERE id = :id", nativeQuery = true)
    void deleteEventDayById(Long id);
}
