package projet.projetstage02.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.projetstage02.model.Problem;

@Repository
public interface ProblemsRepository extends JpaRepository<Problem, Long> {
}
