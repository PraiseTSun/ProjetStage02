package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    //TODO when merging with token branch, user TimeUtil stuff
    private final long MILLI_SECOND_DAY = 864000000;
    public void saveStudent(String firstName,
                            String lastName,
                            String email,
                            String password,
                            AbstractUser.Department department) {
        StudentDTO dto = StudentDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email.toLowerCase())
                .password(password)
                .department(department.departement)
                .isConfirmed(false)
                .emailConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        saveStudent(dto);
    }

    public long saveStudent(StudentDTO dto) {
        return studentRepository.save(dto.toModel()).getId();
    }

    public boolean isEmailUnique(String email) {
        return studentRepository.findByEmail(email).isEmpty();
    }

    public StudentDTO getStudentById(Long id) throws NonExistentUserException {
        var studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentUserException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO getStudentByEmailPassword(String email, String password) throws NonExistentUserException {
        var studentOpt = studentRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty()) throw new NonExistentUserException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO uploadCurriculumVitae(StudentDTO dto) throws NonExistentUserException {
        Student student = getStudentById(dto.getId()).toModel();
        student.setCv(dto.getCv());
        saveStudent(new StudentDTO(student));
        return new StudentDTO(student);
    }

    public boolean deleteUnconfirmedStudent(StudentDTO dto) throws NonExistentUserException {
        Optional<Student> studentOpt = studentRepository.findById(dto.getId());
        if(studentOpt.isEmpty()) throw new NonExistentUserException();
        Student student = studentOpt.get();
        if(!student.isEmailConfirmed() && Timestamp.valueOf(LocalDateTime.now()).getTime() - student.getInscriptionTimestamp() > MILLI_SECOND_DAY){
             studentRepository.delete(student);
             return true;
        }
        return false;
    }
}
