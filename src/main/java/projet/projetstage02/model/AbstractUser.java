package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractUser {
    public enum Department {

        Informatique("Techniques de linformatique"),
        Transport("Techniques de la logistique du transport");

        public String departement;

        Department(String departement) {
            this.departement = departement;
        }

        public static Department getDepartment(String departement) {
            return Arrays.stream(
                    Department.values()).filter(
                            department -> department.departement.equals(departement))
                    .toList().get(0);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected long id;
    @NotBlank
    @Size(min = 2)
    protected String firstName;
    @NotBlank
    @Size(min = 2)
    protected String lastName;
    @NotBlank
    @Email
    protected String email;
    @ToString.Exclude
    @NotBlank
    @Size(min = 2)
    protected String password;
    protected boolean isConfirm;
    protected long inscriptionTimestamp;
    protected boolean emailConfirmed;

    public AbstractUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        inscriptionTimestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        isConfirm = false;
        emailConfirmed = false;
    }

    public AbstractUser(String firstName, String lastName, String email, String password, long inscriptionTimestamp,
            boolean emailConfirmed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.inscriptionTimestamp = inscriptionTimestamp;
        isConfirm = false;
        this.emailConfirmed = emailConfirmed;
    }
}
