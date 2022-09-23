package projet.projetstage02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends AbstractUser {
    private Department department;
    private String name;

    public Company(String firstName, String lastName, String email, String password, Department department,
            String name) {
        super(firstName, lastName, email, password);
        this.department = department;
        this.name = name;
    }

    public Company(
            String firstName,
            String lastName,
            String email,
            String password,
            Department department,
            String name,
            long inscriptionTimestamp,
            boolean emailConfirmed) {
        super(firstName, lastName, email, password, inscriptionTimestamp, emailConfirmed);
        this.department = department;
        this.name = name;
    }
}
