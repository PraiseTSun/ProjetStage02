package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class StudentService extends AbstractService<StudentDTO> {
    private final UserRepository userRepository;

    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUniqueEmail(String email) {
       Optional<Student> student = userRepository.findStudentByEmail(email);
        return student.isEmpty();
    }

    public void saveStudent(String firstName, String lastName, String email, String password,
            AbstractUser.Department department) {
        StudentDTO dto = new StudentDTO(
                firstName,
                lastName,
                email.toLowerCase(),
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime(),
                department.departement);
        saveStudent(dto);
    }

    public long saveStudent(StudentDTO dto) {
        return userRepository.save(dto.getClassOrigin()).getId();
    }

    @Override
    public StudentDTO getUserById(Long id) {
        var studentOpt = userRepository.findStudentById(id);
        if (studentOpt.isEmpty())
            return null;
        return new StudentDTO(studentOpt.get());
    }

    @Override
    public StudentDTO getUserByEmailPassword(String email, String password) {
        var studentOpt = userRepository.findStudentByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty())
            return null;
        return new StudentDTO(studentOpt.get());
    }
}
