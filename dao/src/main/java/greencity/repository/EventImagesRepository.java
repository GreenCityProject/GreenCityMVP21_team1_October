package greencity.repository;

import greencity.entity.EventImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImagesRepository extends JpaRepository<EventImages, Long> {

    /**
     * Method removes event images by event id
     *
     * @param id
     */
    void deleteEventImagesByEvent_Id(Long id);
}
