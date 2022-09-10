package projet.projetstage02.service;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.repository.GestionnaireRepository;

@Component
public class GestionnaireService {
    private GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public void createGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = new GestionnaireDTO(firstname, lastname, email, password, false);
        createGestionnaire(dto);
    }
    public void createGestionnaire(GestionnaireDTO dto) {
        gestionnaireRepository.save(dto.getOrigin());
    }

    public void ConfirmUser(AbstractUser user) {
        if(user.getClass() == Gestionnaire.class)
            ConfirmGestionaire(user);
    }

    private void ConfirmGestionaire(AbstractUser user) {
        var gestionnaireOpt = gestionnaireRepository.findById(user.getId());
        if(gestionnaireOpt.isEmpty())
            return;
        Gestionnaire gestionnaire = gestionnaireOpt.get();
        gestionnaire.setIsConfirm(true);
        gestionnaireRepository.save(gestionnaire);
    }

    public Gestionnaire getGestionnaire(Long id) {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if(gestionnaireOpt.isEmpty())
            return null;
        return gestionnaireOpt.get();
    }
}
