package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.modele.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
