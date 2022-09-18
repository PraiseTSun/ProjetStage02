package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projet.projetstage02.modele.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailAndPassword(String email, String password);

    Optional<Student> findByEmail(String email);

    @Query("SELECT student FROM Student student WHERE student.isConfirm = false")
    List<Student> findAllUnvalidatedStudents();
}
