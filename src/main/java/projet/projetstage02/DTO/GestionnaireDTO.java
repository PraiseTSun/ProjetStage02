package projet.projetstage02.DTO;

import projet.projetstage02.modele.Gestionnaire;

public class GestionnaireDTO extends AbstractUserDTO<Gestionnaire> {

    public GestionnaireDTO(Gestionnaire gestionnaire){
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
