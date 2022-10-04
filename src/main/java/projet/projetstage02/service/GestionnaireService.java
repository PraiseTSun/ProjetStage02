package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Gestionnaire;
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
public class GestionnaireService {
    //TODO when merging with token branch, user TimeUtil stuff
    private final long MILLI_SECOND_DAY = 864000000;
    private final GestionnaireRepository gestionnaireRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;

    private final OffreRepository offreRepository;

    public long saveGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = GestionnaireDTO.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email.toLowerCase())
                .password(password)
                .isConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        return saveGestionnaire(dto);
    }

    public long saveGestionnaire(GestionnaireDTO dto) {
        return gestionnaireRepository.save(dto.toModel()).getId();
    }


    public boolean isEmailUnique(String email) {
        return gestionnaireRepository.findByEmail(email).isEmpty();
    }

    public GestionnaireDTO getGestionnaireById(Long id) throws NonExistentEntityException {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public GestionnaireDTO getGestionnaireByEmailPassword(String email, String password) throws NonExistentEntityException {
        var gestionnaireOpt = gestionnaireRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public void validateCompany(Long id) throws NonExistentEntityException {
        //Throws NonExistentUserException
        Company company = getCompany(id);
        company.setConfirm(true);
        companyRepository.save(company);
    }

    public void validateStudent(Long id) throws NonExistentEntityException {
        //Throws NonExistentUserException
        Student student = getStudent(id);
        student.setConfirm(true);
        studentRepository.save(student);
    }

    public void removeCompany(long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Company company = getCompany(id);
        companyRepository.delete(company);
    }

    public void removeStudent(long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Student student = getStudent(id);
        studentRepository.delete(student);
    }

    private Company getCompany(long id) throws NonExistentEntityException {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) throw new NonExistentEntityException();
        return companyOptional.get();
    }

    private Student getStudent(long id) throws NonExistentEntityException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) throw new NonExistentEntityException();
        return studentOptional.get();
    }

    private Offre getOffer(long id) throws NonExistentOfferExeption {
        Optional<Offre> offreOpt = offreRepository.findById(id);
        if (offreOpt.isEmpty()) throw new NonExistentOfferExeption();
        return offreOpt.get();
    }

    public List<OffreDTO> getNoneValidateOffers() {
        List<OffreDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        !offre.isValide())
                .forEach(offre ->
                        offres.add(new OffreDTO(offre)));
        return offres;
    }

    public OffreDTO validateOfferById(Long id) throws NonExistentOfferExeption {
        Offre offre = getOffer(id);
        offre.setValide(true);
        offreRepository.save(offre);

        return new OffreDTO(offre);
    }

    public void removeOfferById(long id) throws NonExistentOfferExeption {
        offreRepository.delete(getOffer(id));
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
                .filter(company ->
                        !company.isConfirm() && company.isEmailConfirmed()
                )
                .forEach(company ->
                        unvalidatedCompaniesDTOs.add(new CompanyDTO(company)));
        return unvalidatedCompaniesDTOs;
    }

    public byte[] getOffrePdfById(long id) throws NonExistentOfferExeption {
        return getOffer(id).getPdf();
    }

    public List<StudentDTO> getUnvalidatedCVStudents() {
        List<StudentDTO> studentDTOS = new ArrayList<>();

        studentRepository.findAll().stream()
                .filter(student ->
                        student.getCvToValidate() != null &&
                                student.getCvToValidate().length > 0
                                && student.isConfirm()
                )
                .forEach(student -> {
                    StudentDTO dto = new StudentDTO(student);
                    dto.setPassword("");
                    dto.setCv(new byte[0]);

                    studentDTOS.add(dto);
                });

        return studentDTOS;
    }

    public StudentDTO validateStudentCV(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        student.setCv(student.getCvToValidate());
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        return new StudentDTO(student);
    }

    public StudentDTO removeStudentCvValidation(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        return new StudentDTO(student);
    }

    public byte[] getStudentCvToValidate(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return studentOpt.get().getCvToValidate();
    }

    public boolean deleteUnconfirmedGestionnaire(GestionnaireDTO dto) throws NonExistentEntityException {
        Optional<Gestionnaire> studentOpt = gestionnaireRepository.findByEmail(dto.getEmail());
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Gestionnaire gestionnaire = studentOpt.get();
        if(!gestionnaire.isEmailConfirmed() && Timestamp.valueOf(LocalDateTime.now()).getTime() - gestionnaire.getInscriptionTimestamp() > MILLI_SECOND_DAY){
            gestionnaireRepository.delete(gestionnaire);
            return true;
        }
        return false;
    }
}
