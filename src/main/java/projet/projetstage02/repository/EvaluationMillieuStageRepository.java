package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.EvaluationMillieuStage;

import java.util.Optional;

public interface EvaluationMillieuStageRepository extends JpaRepository<EvaluationMillieuStage, Long> {
    Optional<EvaluationMillieuStage> findByContractId(long contractId);
}
