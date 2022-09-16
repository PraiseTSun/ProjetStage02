package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.repository.GestionnaireRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class GestionnaireService extends AbstractService<GestionnaireDTO> {
    private GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public void saveGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = new GestionnaireDTO(
                firstname,
                lastname,
                email.toLowerCase(),
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime());
        saveGestionnaire(dto);
    }

    public long saveGestionnaire(GestionnaireDTO dto) {
        return gestionnaireRepository.save(dto.getClassOrigin()).getId();
    }

    public void confirmUser(AbstractUserDTO<Gestionnaire> user) {
        if (user.getClass() == GestionnaireDTO.class)
            confirmGestionaire(user.getClassOrigin());
    }

    private void confirmGestionaire(Gestionnaire user) {
        var gestionnaireOpt = gestionnaireRepository.findById(user.getId());
        if (gestionnaireOpt.isEmpty())
            return;
        Gestionnaire gestionnaire = gestionnaireOpt.get();
        gestionnaire.setConfirm(true);
        gestionnaireRepository.save(gestionnaire);
    }

    @Override
    public boolean isUniqueEmail(String email) {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
        return gestionnaire.isEmpty();
    }

    @Override
    public GestionnaireDTO getUserById(Long id) {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty())
            return null;
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    @Override
    public GestionnaireDTO getUserByEmailPassword(String email, String password) {
        var gestionnaireOpt = gestionnaireRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty())
            return null;
        return new GestionnaireDTO(gestionnaireOpt.get());
    }
}
