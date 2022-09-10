package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.StudentRepository;

@Component
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void createStudent(String firstName, String lastName, String email, String password, AbstractUser.Department department) {
        StudentDTO dto = new StudentDTO(firstName, lastName, email, password, false,department.toString());
        createStudent(dto);
    }
    public void createStudent(StudentDTO dto) {
        studentRepository.save(dto.getOrigin());
    }
}
