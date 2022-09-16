package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Gestionnaire;

import java.util.Optional;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    Optional<Gestionnaire> findByEmailAndPassword(String email, String password);

    Optional<Gestionnaire> findByEmail(String email);
}
