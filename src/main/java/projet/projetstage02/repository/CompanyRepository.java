package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projet.projetstage02.modele.Company;
import projet.projetstage02.modele.Gestionnaire;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(value = "SELECT p FROM Company p " +
            "WHERE p.email = :email AND p.password = :password")
    Optional<Company> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
