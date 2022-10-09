package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
}
