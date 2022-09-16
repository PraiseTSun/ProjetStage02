package projet.projetstage02.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.modele.Gestionnaire;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {

    public GestionnaireDTO(String firstName, String lastName, String email, String password, boolean isConfirmed,
            long inscriptionTimestamp) {
        super("0", firstName, lastName, email, password, isConfirmed, inscriptionTimestamp, false);
    }

    public GestionnaireDTO(Gestionnaire gestionnaire) {
        id = String.valueOf(gestionnaire.getId());
        firstName = gestionnaire.getFirstName();
        lastName = gestionnaire.getLastName();
        email = gestionnaire.getEmail();
        password = gestionnaire.getPassword();
        inscriptionTimestamp = gestionnaire.getInscriptionTimestamp();
        emailConfirmed = gestionnaire.isEmailConfirmed();
        isConfirmed = gestionnaire.isConfirm();
    }

    @Override
    public Gestionnaire getClassOrigin() {
        Gestionnaire gestionnaire = new Gestionnaire(
                firstName,
                lastName,
                email,
                password,
                inscriptionTimestamp,
                emailConfirmed);
        gestionnaire.setId(Long.parseLong(id));
        return gestionnaire;
    }
}
