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
    private String companyName;

    public Company(String firstName, String lastName, String email, String password, Department department,
            String companyName) {
        super(firstName, lastName, email, password);
        this.department = department;
        this.companyName = companyName;
    }

    public Company(
            String firstName,
            String lastName,
            String email,
            String password,
            Department department,
            String companyName,
            long inscriptionTimestamp,
            boolean emailConfirmed) {
        super(firstName, lastName, email, password, inscriptionTimestamp, emailConfirmed);
        this.department = department;
        this.companyName = companyName;
    }
}
