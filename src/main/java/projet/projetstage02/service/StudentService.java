package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.StudentRepository;

@Component
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String firstName, String lastName, String email, String password, AbstractUser.Department department) {
        Student student = new Student(firstName, lastName, email, password, department);
        studentRepository.save(student);
        return student;
    }
}
