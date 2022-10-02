package projet.projetstage02.model;

import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends AbstractUser {
    @NotNull
    private Department department;
    @NotBlank
    @Size(min = 2)
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
