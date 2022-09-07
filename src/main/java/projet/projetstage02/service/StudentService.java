package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.StudentRepository;

@Component
public class StudentService {
    private StudentRepository studentRepository;

    public Student createStudent(String firstName, String lastName, String email, String password) {
        return null;
    }
}
