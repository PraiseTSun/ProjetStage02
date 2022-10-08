package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.PdfDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.StudentRepository;
import projet.projetstage02.utils.TimeUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final OffreRepository offreRepository;
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

    public StudentDTO getStudentById(Long id) throws NonExistentEntityException {
        var studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO getStudentByEmailPassword(String email, String password) throws NonExistentEntityException {
        var studentOpt = studentRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO uploadCurriculumVitae(PdfDTO dto) throws NonExistentEntityException {
        Student student = getStudentById(dto.getStudentId()).toModel();
        student.setCvToValidate(dto.getPdf());
        saveStudent(new StudentDTO(student));
        return new StudentDTO(student);
    }

    public boolean deleteUnconfirmedStudent(StudentDTO dto) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findByEmail(dto.getEmail());
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        if(currentTimestamp() - student.getInscriptionTimestamp() > MILLI_SECOND_DAY){
             studentRepository.delete(student);
             return true;
        }
        return false;
    }
}
