package repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
	boolean existsByName(String name);
	Optional<Section> findByName(String name);
}