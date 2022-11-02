package projet.projetstage02.dto.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.Gestionnaire;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {

    private String token;

    public GestionnaireDTO(Gestionnaire gestionnaire) {
        id = gestionnaire.getId();
        firstName = gestionnaire.getFirstName();
        lastName = gestionnaire.getLastName();
        email = gestionnaire.getEmail();
        password = gestionnaire.getPassword();
        inscriptionTimestamp = gestionnaire.getInscriptionTimestamp();
        emailConfirmed = gestionnaire.isEmailConfirmed();
        isConfirmed = gestionnaire.isConfirm();

    }

    @Override
    public Gestionnaire toModel() {
        Gestionnaire gestionnaire = new Gestionnaire(
                firstName,
                lastName,
                email,
                password,
                inscriptionTimestamp,
                emailConfirmed);
        gestionnaire.setId(id);
        return gestionnaire;
    }
}
