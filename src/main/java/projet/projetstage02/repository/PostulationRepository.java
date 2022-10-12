package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.Postulation;

import java.util.List;
import java.util.Optional;

public interface PostulationRepository extends JpaRepository<Postulation, Long> {
    Optional<Postulation> findByStudentIdAndOfferId(long studentId, long offerId);

    List<Postulation> findByStudentId(long studentId);
}
