package projet.projetstage02.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.modele.Gestionnaire;

@Data
@NoArgsConstructor
public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {

    public GestionnaireDTO(String firstName, String lastName, String email, String password, Boolean isConfirmed) {
        super("0", firstName, lastName, email, password, isConfirmed);
    }

    public GestionnaireDTO(Gestionnaire gestionnaire){
        id = gestionnaire.getId().toString();
        firstName = gestionnaire.getFirstName();
        lastName = gestionnaire.getLastName();
        email = gestionnaire.getEmail();
        password = gestionnaire.getPassword();
    }

    @Override
    public Gestionnaire getOrigin() {
        Gestionnaire gestionnaire = new Gestionnaire(
                firstName,
                lastName,
                email,
                password
        );
        gestionnaire.setId(Long.parseLong(id));
        return gestionnaire;
    }
}
