package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.modele.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AbstractUser, Long> {
    @Query("SELECT company FROM Company company WHERE company.email = :email AND company.password = :password")
    Optional<Company> findCompanyByEmailAndPassword(String email, String password);

    @Query("SELECT student FROM Student student WHERE student.email = :email AND student.password = :password")
    Optional<Student> findStudentByEmailAndPassword(String email, String password);

    @Query("SELECT gestionnaire FROM Gestionnaire gestionnaire WHERE gestionnaire.email = :email AND gestionnaire.password = :password")
    Optional<Gestionnaire> findGestionnaireByEmailAndPassword(String email, String password);

    @Query("SELECT company FROM Company company WHERE company.email = :email")
    Optional<Company> findCompanyByEmail(String email);

    @Query("SELECT student FROM Student student WHERE student.email = :email")
    Optional<Student> findStudentByEmail(String email);

    @Query("SELECT gestionnaire FROM Gestionnaire gestionnaire WHERE gestionnaire.email = :email")
    Optional<Gestionnaire> findGestionnaireByEmail(String email);

    @Query("SELECT company FROM Company company WHERE company.id = :id")
    Optional<Company> findCompanyById(Long id);

    @Query("SELECT student FROM Student student WHERE student.id = :id")
    Optional<Student> findStudentById(Long id);

    @Query("SELECT gestionnaire FROM Gestionnaire gestionnaire WHERE gestionnaire.id = :id")
    Optional<Gestionnaire> findGestionnaireById(Long id);

    @Query("SELECT abstractUser FROM AbstractUser abstractUser WHERE abstractUser.isConfirm = false")
    List<AbstractUser> findAllUnvalidated();
}
