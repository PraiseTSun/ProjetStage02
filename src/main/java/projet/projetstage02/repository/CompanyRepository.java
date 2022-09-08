package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
