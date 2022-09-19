package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.modele.Company;
import projet.projetstage02.modele.Gestionnaire;
import projet.projetstage02.modele.Student;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GestionnaireService extends AbstractService<GestionnaireDTO> {

    private GestionnaireRepository gestionnaireRepository;
    private CompanyRepository companyRepository;
    private StudentRepository studentRepository;

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

    public void confirmUser(AbstractUserDTO<Gestionnaire> user) throws NonExistentUserException {
        if (user.getClass() == GestionnaireDTO.class)
            confirmGestionaire(user.getClassOrigin());
    }

    private void confirmGestionaire(Gestionnaire user) throws NonExistentUserException {
        var gestionnaireOpt = gestionnaireRepository.findById(user.getId());
        if (gestionnaireOpt.isEmpty()) throw new NonExistentUserException();
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
    public GestionnaireDTO getUserById(Long id) throws NonExistentUserException {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentUserException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    @Override
    public GestionnaireDTO getUserByEmailPassword(String email, String password) throws NonExistentUserException {
        var gestionnaireOpt = gestionnaireRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentUserException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    @Override
    public List<GestionnaireDTO> getUnvalidatedUsers() {
        List<GestionnaireDTO> unvalidatedGestionnaireDTOs = new ArrayList<>();
        gestionnaireRepository.findAllUnvalidatedGestionnaires()
                .forEach(gestionnaire -> unvalidatedGestionnaireDTOs.add(new GestionnaireDTO(gestionnaire)));
        return unvalidatedGestionnaireDTOs;
    }

    public void validateGestionnaire(Long id) throws NonExistentUserException {
        Optional<Gestionnaire> gestionnaireOptional = gestionnaireRepository.findById(id);
        if (gestionnaireOptional.isEmpty()) throw new NonExistentUserException();
        else {
            Gestionnaire gestionnaire = gestionnaireOptional.get();
            gestionnaire.setConfirm(true);
            gestionnaireRepository.save(gestionnaire);
        }
    }

    public void validateCompany(Long id) throws NonExistentUserException {
        //Throws NonExistentUserException
        Company company = getCompany(id);
        company.setConfirm(true);
        companyRepository.save(company);
    }

    public void validateStudent(Long id) throws NonExistentUserException {
        //Throws NonExistentUserException
        Student student = getStudent(id);
        student.setConfirm(true);
        studentRepository.save(student);
    }

    public void removeCompany(long id) throws NonExistentUserException {
        // Throws NonExistentUserException
        Company company = getCompany(id);
        companyRepository.delete(company);
    }

    public void removeStudent(long id) throws NonExistentUserException {
        // Throws NonExistentUserException
        Student student = getStudent(id);
        studentRepository.delete(student);
    }

    private Company getCompany(long id) throws NonExistentUserException {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) throw new NonExistentUserException();
        return companyOptional.get();
    }

    private Student getStudent(long id) throws NonExistentUserException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) throw new NonExistentUserException();
        return studentOptional.get();
    }
}
