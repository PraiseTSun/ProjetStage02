package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Offre;

public interface OffreRepository extends JpaRepository<Offre, Long> {
}