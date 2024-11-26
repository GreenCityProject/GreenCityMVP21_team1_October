package greencity.repository;

import greencity.entity.Event;
import greencity.enums.EventStatus;
import greencity.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;



public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Method removes eventDays by event id
     *
     * @param id
     */
    @Modifying
    @Query(value = "DELETE FROM event_days WHERE id = :id", nativeQuery = true)
    void deleteEventDayByEventId(Long id);


    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId")
    Page<Event> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId AND e.type = :type")
    Page<Event> findByUserIdAndEventType(@Param("userId") Long userId, @Param("type") EventType type, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId AND e.status = :status")
    List<Event> findByCreatorIdAndStatus(@Param("userId") Long userId, @Param("status") EventStatus status);

    @Query("SELECT e FROM Event e JOIN e.attendants a WHERE a.id = :userId AND e.status = :status")
    List<Event> findByAttendeesUserIdAndStatus(@Param("userId") Long userId, @Param("status") EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId AND e.startTime > :now")
    List<Event> findByUserIdAndStartTimeAfter(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId AND e.endTime < :now")
    List<Event> findByUserIdAndEndTimeBefore(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :userId AND e.startTime < :now AND e.endTime > :now")
    List<Event> findByUserIdAndStartTimeBeforeAndEndTimeAfter(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}

