package greencity.repository.impl;

import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.repository.CustomEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements CustomEventRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Event> findFilteredEvents(String eventTime, String location, List<String> tags, String status, UserVO currentUser) {

        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Event e JOIN e.eventDays ed WHERE 1=1");

        Map<String, Object> parameters = new HashMap<>();

        // Filter by time
        if ("Upcoming".equalsIgnoreCase(eventTime)) {
            queryBuilder.append(" AND ed.eventDate > :currentDate");
            parameters.put("currentDate", LocalDate.now());
        } else if ("Passed".equalsIgnoreCase(eventTime)) {
            queryBuilder.append(" AND ed.eventDate < :currentDate");
            parameters.put("currentDate", LocalDate.now());
        }

        // Filter by location (online or city)
        if (location != null) {
            if ("Online".equalsIgnoreCase(location)) {
                queryBuilder.append(" AND e.isOnline = true");
            } //else {
//                queryBuilder.append(" AND ed.city = :city");
//                parameters.put("city", location);
//            }
        }

        // Filter by status
        if (status != null) {
            if (currentUser == null) {
                if ("Open".equalsIgnoreCase(status)) {
                    queryBuilder.append(" AND e.isOpen = true");
                } else if ("Closed".equalsIgnoreCase(status)) {
                    queryBuilder.append(" AND e.isOpen = false");
                }
            } else {
                switch (status) {
                    case "Open" -> queryBuilder.append(" AND e.isOpen = true");
                    case "Closed" -> queryBuilder.append(" AND e.isOpen = false");
//                    case "Joined" -> {
//                        queryBuilder.append(" AND :userId MEMBER OF e.attendants");
//                        parameters.put("userId", currentUser.getId());
//                    }
//                    case "Saved" -> queryBuilder.append(" AND e.isFavorite = true");
//                    case "Created" -> {
//                        queryBuilder.append(" AND e.organizer.id = :organizerId");
//                        parameters.put("organizerId", currentUser.getId());
//                    }
                }
            }
        }

        // Filter by tags
        if (tags != null && !tags.isEmpty()) {
            queryBuilder.append(" AND e.tags.name IN :tags");
            parameters.put("tags", tags);
        }

        TypedQuery<Event> query = entityManager.createQuery(queryBuilder.toString(), Event.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
}
