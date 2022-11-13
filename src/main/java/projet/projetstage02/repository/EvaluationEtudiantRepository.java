package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.EvaluationEtudiant;

import java.util.Optional;

public interface EvaluationEtudiantRepository extends JpaRepository<EvaluationEtudiant, Long> {
    Optional<EvaluationEtudiant> findByContractId(long id);
}
