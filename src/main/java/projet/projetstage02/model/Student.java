package projet.projetstage02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
public class Student extends AbstractUser {
    @NotNull
    private Department department;
    @NotNull
    @Lob
    private byte[] cv;
    public Student(String firstName, String lastName, String email, String password, Department department) {
        super(firstName, lastName, email, password);
        this.department = department;
    }

    public Student(
            String firstName,
            String lastName,
            String email,
            String password,
            Department department,
            long inscriptionTimestamp,
            boolean emailConfirmed) {
        super(firstName, lastName, email, password, inscriptionTimestamp, emailConfirmed);
        this.department = department;
    }
}
