package projet.projetstage02.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.modele.Gestionnaire;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {

    public GestionnaireDTO(String firstName, String lastName, String email, String password, Boolean isConfirmed) {
        super("0", firstName, lastName, email, password, isConfirmed);
    }

    public GestionnaireDTO(Gestionnaire gestionnaire){
        id = String.valueOf(gestionnaire.getId());
        firstName = gestionnaire.getFirstName();
        lastName = gestionnaire.getLastName();
        email = gestionnaire.getEmail();
        password = gestionnaire.getPassword();
        isConfirmed = gestionnaire.isConfirm();
    }

    @Override
    public Gestionnaire getClassOrigin() {
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
