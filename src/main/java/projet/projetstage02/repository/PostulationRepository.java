package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.Postulation;

public interface PostulationRepository extends JpaRepository<Postulation, Long> {
}
