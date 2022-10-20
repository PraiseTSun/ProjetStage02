package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.ApplicationAcceptation;

import java.util.Optional;

public interface ApplicationAcceptationRepository extends JpaRepository<ApplicationAcceptation, Long> {
    Optional<ApplicationAcceptation> findByOfferIdAndStudentId(long offerId, long studentId);
}
