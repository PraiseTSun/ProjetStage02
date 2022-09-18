package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projet.projetstage02.modele.Gestionnaire;

import java.util.List;
import java.util.Optional;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    Optional<Gestionnaire> findByEmailAndPassword(String email, String password);

    Optional<Gestionnaire> findByEmail(String email);

    @Query("SELECT gestionnaire FROM Gestionnaire gestionnaire WHERE gestionnaire.isConfirm = false")
    List<Gestionnaire> findAllUnvalidatedGestionnaires();
}
