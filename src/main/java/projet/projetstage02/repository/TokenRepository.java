package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.projetstage02.model.Token;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, String> {
}
