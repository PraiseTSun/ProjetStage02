package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.EvaluationMilieuDeStagePDF;
import projet.projetstage02.model.EvaluationPDF;

import java.util.Optional;

public interface EvaluationMillieuStagePDFRepository extends JpaRepository<EvaluationMilieuDeStagePDF, Long> {
    Optional<EvaluationPDF> findByContractId(long id);

}
