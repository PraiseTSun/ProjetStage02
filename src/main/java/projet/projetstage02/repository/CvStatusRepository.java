package projet.projetstage02.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.projetstage02.model.CvStatus;

@Repository
public interface CvStatusRepository extends JpaRepository<CvStatus, Long> {
}
