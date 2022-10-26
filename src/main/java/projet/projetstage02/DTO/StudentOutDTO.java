package projet.projetstage02.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Student;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static projet.projetstage02.utils.ByteConverter.StringToBytes;
import static projet.projetstage02.utils.ByteConverter.byteToString;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class StudentOutDTO extends AbstractUserDTO<Student> {
    @NotBlank
    @Pattern(regexp = ("Techniques de linformatique|Techniques de la logistique du transport"))
    private String department;
    @Lob
    private String cv;
    @Lob
    private String cvToValidate;

    public StudentOutDTO(Student student) {
        id = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        email = student.getEmail();
        password = student.getPassword();
        isConfirmed = student.isConfirm();
        department = student.getDepartment().departement;
        inscriptionTimestamp = student.getInscriptionTimestamp();
        emailConfirmed = student.isEmailConfirmed();
        cv = byteToString(student.getCv());
        cvToValidate = byteToString(student.getCvToValidate());
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
        student.setConfirm(isConfirmed);
        student.setEmailConfirmed(emailConfirmed);
        student.setId(id);
        student.setCv(StringToBytes(cv));
        student.setCvToValidate(StringToBytes(cvToValidate));
        return student;
    }
}
