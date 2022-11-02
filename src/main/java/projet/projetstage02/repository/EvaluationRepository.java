package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
