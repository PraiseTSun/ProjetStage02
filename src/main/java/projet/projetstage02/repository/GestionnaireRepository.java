package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projet.projetstage02.modele.Gestionnaire;

import java.util.Optional;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    @Query(value = "SELECT p FROM Gestionnaire p " +
            "WHERE p.email = :email AND p.password = :password")
    Optional<Gestionnaire> findByEmailAndPassword(@Param("email") String email,@Param("password") String password);
}
