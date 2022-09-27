package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.OffreValidateDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GestionnaireService{

    private final GestionnaireRepository gestionnaireRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;

    private final OffreRepository offreRepository;

    public void saveGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = GestionnaireDTO.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email.toLowerCase())
                .password(password)
                .isConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        saveGestionnaire(dto);
    }

    public long saveGestionnaire(GestionnaireDTO dto) {
        return gestionnaireRepository.save(dto.toModel()).getId();
    }


    public boolean isEmailUnique(String email) {
        return gestionnaireRepository.findByEmail(email).isEmpty();
    }

    public GestionnaireDTO getGestionnaireById(Long id) throws NonExistentUserException {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentUserException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public GestionnaireDTO getGestionnaireByEmailPassword(String email, String password) throws NonExistentUserException {
        var gestionnaireOpt = gestionnaireRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentUserException();
        return new GestionnaireDTO(gestionnaireOpt.get());
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

    public List<OffreValidateDTO> getNoneValidateOffers() {
        List<OffreValidateDTO> offres = new ArrayList<>();
        offreRepository.findAll()
                .forEach(offre -> {
                    if (!offre.isValide())
                        offres.add(new OffreValidateDTO(offre));
                });
        return offres;
    }

    public OffreDTO valideOfferById(Long id) {
        Optional<Offre> offreOpt = offreRepository.findById(id);
        if(offreOpt.isEmpty())
            return null;

        Offre offre = offreOpt.get();
        offre.setValide(true);
        offreRepository.save(offre);

        return new OffreDTO(offre);
    }

    public void removeOfferById(long id) {
        Optional<Offre> offreOpt = offreRepository.findById(id);
        if (offreOpt.isEmpty())
            throw new RuntimeException("Offre do not exist");
        Offre offre = offreOpt.get();
        offreRepository.delete(offre);
    }

    public List<StudentDTO> getUnvalidatedStudents() {
        List<StudentDTO> unvalidatedStudentDTOs = new ArrayList<>();
        studentRepository.findAll().stream()
                .filter(student ->
                        student.isEmailConfirmed() && !student.isConfirm()
                )
                .forEach(student ->
                        unvalidatedStudentDTOs.add(new StudentDTO(student)));
        return unvalidatedStudentDTOs;
    }

    public List<CompanyDTO> getUnvalidatedCompanies() {
        List<CompanyDTO> unvalidatedCompaniesDTOs = new ArrayList<>();
        companyRepository.findAll().stream()
                .filter(company->
                        !company.isConfirm() && company.isEmailConfirmed()
                )
                .forEach(company ->
                        unvalidatedCompaniesDTOs.add(new CompanyDTO(company)));
        return unvalidatedCompaniesDTOs;
    }
}
