package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Gestionnaire;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
}
