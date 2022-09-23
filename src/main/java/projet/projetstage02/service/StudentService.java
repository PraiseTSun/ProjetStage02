package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public void saveStudent(String firstName,
                            String lastName,
                            String email,
                            String password,
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
        return studentRepository.save(dto.getClassOrigin()).getId();
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

    public List<StudentDTO> getUnvalidatedStudent() {
        List<StudentDTO> unvalidatedStudentDTOs = new ArrayList<>();
        studentRepository.findAll().stream()
                .filter(student ->
                        student.isEmailConfirmed() && !student.isConfirm()
                )
                .forEach(student ->
                        unvalidatedStudentDTOs.add(new StudentDTO(student)));
        return unvalidatedStudentDTOs;
    }
}
