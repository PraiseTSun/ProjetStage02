package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.EvaluationEtudiantPDF;
import projet.projetstage02.model.EvaluationPDF;

import java.util.Optional;

public interface EvaluationEtudiantPDFRepository extends JpaRepository<EvaluationEtudiantPDF, Long> {
    Optional<EvaluationPDF> findByContractId(long id);
}
