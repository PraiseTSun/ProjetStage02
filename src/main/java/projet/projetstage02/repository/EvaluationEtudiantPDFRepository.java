package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.EvaluationPDF;

public interface EvaluationEtudiantPDFRepository extends JpaRepository<EvaluationPDF, Long> {
}
