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
        if (eventTime != null) {
            if ("upcoming".equalsIgnoreCase(eventTime)) {
                queryBuilder.append(" AND ed.eventDate >= :currentDate");
                parameters.put("currentDate", LocalDate.now());
            } else if ("passed".equalsIgnoreCase(eventTime)) {
                queryBuilder.append(" AND ed.eventDate < :currentDate");
                parameters.put("currentDate", LocalDate.now());
            }
        }

        // Filter by location (online or city)
        if (location != null) {
            if ("online".equalsIgnoreCase(location)) {
                queryBuilder.append(" AND ed.isOnline = true");
            } else {
                queryBuilder.append(" AND LOWER(ed.location.address) LIKE LOWER(:location)");
                parameters.put("location" +
                        "", "%" + location + "%");
            }
        }

        // Filter by status
        if (status != null) {
            String lowerStatus = status.toLowerCase();
            if (currentUser == null) {
                if ("open".equals(lowerStatus)) {
                    queryBuilder.append(" AND e.isOpen = true");
                } else if ("closed".equals(lowerStatus)) {
                    queryBuilder.append(" AND e.isOpen = false");
                }
            } else {
                switch (lowerStatus) {
                    case "open" -> queryBuilder.append(" AND e.isOpen = true");
                    case "closed" -> queryBuilder.append(" AND e.isOpen = false");
                    //cases "joined" and "saved" for future functionality
//                    case "joined" -> {
//                        queryBuilder.append(" AND :userId MEMBER OF e.attendants");
//                        parameters.put("userId", currentUser.getId());
//                    }
//                    case "saved" -> queryBuilder.append(" AND e.isFavorite = true");
                    case "created" -> {
                        queryBuilder.append(" AND e.organizer.id = :organizerId");
                        parameters.put("organizerId", currentUser.getId());
                    }
                    default -> throw new IllegalArgumentException("Invalid status value: " + status);
                }
            }
        }

        // Filter by tags
        if (tags != null && !tags.isEmpty()) {
            queryBuilder.append(" AND EXISTS (SELECT 1 FROM e.tags t JOIN t.tagTranslations tt WHERE LOWER(tt.name) IN :tags)");
            List<String> lowerCaseTags = tags.stream()
                    .map(String::toLowerCase)
                    .toList();
            parameters.put("tags", lowerCaseTags);
        }

        TypedQuery<Event> query = entityManager.createQuery(queryBuilder.toString(), Event.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
}
