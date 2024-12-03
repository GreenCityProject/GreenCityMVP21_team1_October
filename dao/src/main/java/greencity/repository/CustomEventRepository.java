package greencity.repository;

import greencity.dto.user.UserVO;
import greencity.entity.Event;

import java.util.List;

public interface CustomEventRepository {
    List<Event> findFilteredEvents(String eventTime, String location, List<String> tags, String status, UserVO currentUser);
}
