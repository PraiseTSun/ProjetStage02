package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projet.projetstage02.modele.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailAndPassword( String email, String password);
}
