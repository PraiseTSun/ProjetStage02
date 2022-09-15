package projet.projetstage02.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Gestionnaire extends AbstractUser{
    public Gestionnaire(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
    public Gestionnaire(
            String firstName,
            String lastName,
            String email,
            String password,
            long inscriptionTimestamp,
            boolean emailConfirmed) {
        super(firstName, lastName, email, password,inscriptionTimestamp,emailConfirmed);
    }
}
