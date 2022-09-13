package projet.projetstage02.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy =  InheritanceType.JOINED)
public abstract class AbstractUser {
    public enum Department {
        Informatique,
        Civil,
        Infirmier
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected long id;
    protected String firstName;
    protected String lastName;
    protected String email;
    @ToString.Exclude
    protected String password;
    protected boolean isConfirm;
    protected Timestamp inscriptionTimestamp;
    protected Timestamp confirmationTimestamp;

    public AbstractUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        inscriptionTimestamp = new Timestamp(new Date().getTime());
        isConfirm = false;
    }
}
