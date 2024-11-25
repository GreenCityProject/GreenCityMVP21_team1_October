package greencity.repository;

import greencity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Method removes eventDays by event id
     *
     * @param id
     */
    @Modifying
    @Query(value = "DELETE FROM event_days WHERE id = :id", nativeQuery = true)
    void deleteEventDayByEventId(Long id);

    List<Event> findAllByOrganizer_Id(Long organizerId);


    List<Event> findAllByAttendants_Id(Long attendeeId);


}
