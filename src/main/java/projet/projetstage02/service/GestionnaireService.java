package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.repository.GestionnaireRepository;

@Component
public class GestionnaireService {
    private GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public Gestionnaire createGestionnaire(String firstname, String lastname, String email, String password) {
        return null;
    }
}
