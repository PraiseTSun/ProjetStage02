package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projet.projetstage02.modele.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmailAndPassword(String email, String password);

    Optional<Company> findByEmail(String email);

    @Query("SELECT company FROM Company company WHERE company.isConfirm = false")
    List<Company> findAllUnvalidatedCompanies();
}
