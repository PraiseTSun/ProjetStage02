package projet.projetstage02.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.Gestionnaire;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {


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
