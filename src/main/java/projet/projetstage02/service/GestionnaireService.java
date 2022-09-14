package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.repository.GestionnaireRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Component
public class GestionnaireService extends AbstractService<GestionnaireDTO>{
    private GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public void createGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = new GestionnaireDTO(
                firstname,
                lastname,
                email,
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime());
        createGestionnaire(dto);
    }
    public void createGestionnaire(GestionnaireDTO dto) {
        gestionnaireRepository.save(dto.getOrigin());
    }

    public void confirmUser(AbstractUser user) {
        if(user.getClass() == Gestionnaire.class)
            confirmGestionaire(user);
    }

    private void confirmGestionaire(AbstractUser user) {
        var gestionnaireOpt = gestionnaireRepository.findById(user.getId());
        if(gestionnaireOpt.isEmpty())
            return;
        Gestionnaire gestionnaire = gestionnaireOpt.get();
        gestionnaire.setConfirm(true);
        gestionnaireRepository.save(gestionnaire);
    }

    public Gestionnaire getGestionnaire(Long id) {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if(gestionnaireOpt.isEmpty())
            return null;
        return gestionnaireOpt.get();
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return false;
    }

    @Override
    public GestionnaireDTO getUserById(Long id) {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if(gestionnaireOpt.isEmpty())
            return null;
        return new GestionnaireDTO(gestionnaireOpt.get());
    }
}
