package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailAndPassword(String email, String password);

    Optional<Student> findByEmail(String email);
}
