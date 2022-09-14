package projet.projetstage02.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Student;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class StudentDTO extends AbstractUserDTO<Student> {
    private String department;

    public StudentDTO(String firstName, String lastName, String email, String password, boolean isConfirmed,long inscriptionTimeStamp, String department) {
        super("0", firstName, lastName, email, password, isConfirmed,inscriptionTimeStamp,false);
        this.department = department;
    }


    public StudentDTO(Student student){
        id = String.valueOf(student.getId());
        firstName = student.getFirstName();
        lastName = getLastName();
        email = student.getEmail();
        password = student.getPassword();
        isConfirmed = student.isConfirm();
        department = student.getDepartment().toString();
    }

    @Override
    public Student getOrigin() {
        Student student = new Student(
                firstName,
                lastName,
                email,
                password,
                AbstractUser.Department.valueOf(department),
                inscriptionTimestamp,
                emailConfirmed

        );
        student.setId(Long.parseLong(id));
        return student;
    }
}
