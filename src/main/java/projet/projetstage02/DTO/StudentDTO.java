package projet.projetstage02.DTO;

import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;

public class StudentDTO extends AbstractUserDTO<Student> {
    private String department;

    public StudentDTO(Student student){
        firstName = student.getFirstName();
        lastName = getLastName();
        email = student.getEmail();
        password = student.getPassword();
        department = student.getDepartment().toString();
    }

    @Override
    public Student GetClass() {
        Student student = new Student(
                firstName,
                lastName,
                email,
                password,
                AbstractUser.Department.valueOf(department)
        );
        student.setId(Long.parseLong(id));
        return student;
    }
}
