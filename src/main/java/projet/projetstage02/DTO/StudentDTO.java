package projet.projetstage02.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Student;

import javax.persistence.Lob;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class StudentDTO extends AbstractUserDTO<Student> {
    private String department;
    @Lob
    private byte[] cv;
    @Lob
    private byte[] cvToValidate;

    public StudentDTO(Student student) {
        id = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        email = student.getEmail();
        password = student.getPassword();
        isConfirmed = student.isConfirm();
        department = student.getDepartment().departement;
        inscriptionTimestamp = student.getInscriptionTimestamp();
        emailConfirmed = student.isEmailConfirmed();
        cv = student.getCv();
        cvToValidate = student.getCvToValidate();
    }

    @Override
    public Student toModel() {
        Student student = new Student(
                firstName,
                lastName,
                email,
                password,
                Department.getDepartment(department),
                inscriptionTimestamp,
                emailConfirmed
        );
        student.setId(id);
        student.setCv(cv);
        student.setCvToValidate(cvToValidate);
        return student;
    }
}
