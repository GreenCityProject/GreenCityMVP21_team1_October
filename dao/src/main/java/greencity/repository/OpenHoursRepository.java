package greencity.repository;

import greencity.entity.OpenHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenHoursRepository extends JpaRepository<OpenHours, Long> {
}
