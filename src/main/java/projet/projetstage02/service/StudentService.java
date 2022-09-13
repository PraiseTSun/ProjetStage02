package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.StudentRepository;

@Component
public class StudentService extends AbstractService<StudentDTO> {
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

    @Override
    public StudentDTO getUserById(Long id) {
        var studentOpt = studentRepository.findById(id);
        if(studentOpt.isEmpty())
            return null;
        return new StudentDTO(studentOpt.get());
    }

    @Override
    public StudentDTO getUserByEmailPassword(String email, String password) {
        var studentOpt = studentRepository.findByEmailAndPassword(email, password);
        if(studentOpt.isEmpty())
            return null;
        return new StudentDTO(studentOpt.get());
    }
}
