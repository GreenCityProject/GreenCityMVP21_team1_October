package greencity.repository;

import greencity.entity.OpenHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenHoursRepository extends JpaRepository<OpenHours, String> {
}
