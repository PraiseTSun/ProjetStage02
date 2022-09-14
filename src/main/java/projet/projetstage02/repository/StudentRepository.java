package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projet.projetstage02.modele.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT p FROM Student p " +
            "WHERE p.email = :email AND p.password = :password")
    Optional<Student> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
