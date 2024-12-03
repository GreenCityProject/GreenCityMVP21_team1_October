package greencity.repository;

import greencity.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long>, CustomEventRepository  {

    /**
     * Method removes eventDays by event id
     *
     * @param id
     */
    @Modifying
    @Query(value = "DELETE FROM event_days WHERE id = :id", nativeQuery = true)
    void deleteEventDayByEventId(Long id);

    /**
     * Retrieves all upcoming events based on their event date and start time.
     *
     * <p>An event is considered upcoming if:
     * - Its event date is in the future, or
     * - Its event date is today and its start time is later than the current time.
     *
     * @param pageable pagination parameters for the query.
     * @return a paginated list of upcoming events.
     */
    @Query("SELECT e FROM Event e JOIN e.eventDays ed WHERE ed.eventDate > CURRENT_DATE " +
            "OR (ed.eventDate = CURRENT_DATE AND ed.eventStartTime > CURRENT_TIME)")
    Page<Event> findUpcomingEvents(Pageable pageable);

    /**
     * Retrieves all passed events based on their event date and end time.
     *
     * <p>An event is considered passed if:
     * - Its event date is in the past, or
     * - Its event date is today and its end time is earlier than the current time.
     *
     * @param pageable pagination parameters for the query.
     * @return a paginated list of passed events.
     */
    @Query("SELECT e FROM Event e JOIN e.eventDays ed WHERE ed.eventDate < CURRENT_DATE " +
            "OR (ed.eventDate = CURRENT_DATE AND ed.eventEndTime < CURRENT_TIME)")
    Page<Event> findPassedEvents(Pageable pageable);
}
