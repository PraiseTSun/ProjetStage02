package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GestionnaireService extends AbstractService<GestionnaireDTO> {
    private final UserRepository userRepository;

    public GestionnaireService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.save(dto.getClassOrigin()).getId();
    }

    public void confirmUser(AbstractUserDTO<Gestionnaire> user) {
        if (user.getClass() == GestionnaireDTO.class)
            confirmGestionaire(user.getClassOrigin());
    }

    private void confirmGestionaire(Gestionnaire user) {
        var gestionnaireOpt = userRepository.findGestionnaireById(user.getId());
        if (gestionnaireOpt.isEmpty())
            return;
        Gestionnaire gestionnaire = gestionnaireOpt.get();
        gestionnaire.setConfirm(true);
        userRepository.save(gestionnaire);
    }

    @Override
    public boolean isUniqueEmail(String email) {
        Optional<Gestionnaire> gestionnaire = userRepository.findGestionnaireByEmail(email);
        return gestionnaire.isEmpty();
    }

    @Override
    public GestionnaireDTO getUserById(Long id) {
        var gestionnaireOpt = userRepository.findGestionnaireById(id);
        if (gestionnaireOpt.isEmpty())
            return null;
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    @Override
    public GestionnaireDTO getUserByEmailPassword(String email, String password) {
        var gestionnaireOpt = userRepository.findGestionnaireByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty())
            return null;
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public List<AbstractUserDTO> getUnvalidatedUsers() {
        List<AbstractUser> unvalidatedUsers = userRepository.findAllUnvalidated();
        List<AbstractUserDTO> unvalidatedUserDTOs = new ArrayList<>();

        unvalidatedUsers.forEach(user -> {
            if (user instanceof Company)
                unvalidatedUserDTOs.add(new CompanyDTO((Company) user));
            else if (user instanceof Student)
                unvalidatedUserDTOs.add(new StudentDTO((Student) user));
            else
                unvalidatedUserDTOs.add(new GestionnaireDTO((Gestionnaire) user));
        });
        return unvalidatedUserDTOs;
    }

    public void validateUser(long id) throws NonExistentUserException {
        Optional<AbstractUser> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) throw new NonExistentUserException();
        else {
            AbstractUser user = userOptional.get();
            user.setConfirm(true);
            userRepository.save(user);
        }
    }
}
