package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.ExpiredSessionException;
import projet.projetstage02.exception.InvalidStatusException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.*;

@Service
@AllArgsConstructor
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final CvStatusRepository cvStatusRepository;
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

    private boolean isEmailUnique(String email) {
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
        // Throws NonExistentUserException
        Company company = getCompany(id);
        company.setConfirm(true);
        companyRepository.save(company);
    }

    public void validateStudent(Long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
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

    public List<OffreOutDTO> getUnvalidatedOffers() {
        List<OffreOutDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        !offre.isValide() && isRightSession(offre.getSession(), getNextYear()))
                .forEach(offre ->
                        offres.add(new OffreOutDTO(offre)));
        offres.forEach(offre -> offre.setPdf(byteToString(new byte[0])));
        return offres;
    }

    public List<OffreOutDTO> getValidatedOffers(int year) {
        List<OffreOutDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        offre.isValide() && isRightSession(offre.getSession(), year))
                .forEach(offre ->
                        offres.add(new OffreOutDTO(offre)));
        offres.forEach(offre -> offre.setPdf(byteToString(new byte[0])));
        return offres;
    }


    public OffreOutDTO validateOfferById(Long id) throws NonExistentOfferExeption, ExpiredSessionException {

        Offre offre = getOffer(id);
        if (!isRightSession(offre.getSession(), getNextYear())) {
            throw new ExpiredSessionException();
        }
        offre.setValide(true);
        offreRepository.save(offre);

        return new OffreOutDTO(offre);
    }

    public void removeOfferById(long id) throws NonExistentOfferExeption {
        offreRepository.delete(getOffer(id));
    }

    public List<StudentOutDTO> getUnvalidatedStudents() {
        List<StudentOutDTO> unvalidatedStudentDTOs = new ArrayList<>();
        studentRepository.findAll().stream()
                .filter(student ->
                        student.isEmailConfirmed() && !student.isConfirm()
                )
                .forEach(student ->
                        unvalidatedStudentDTOs.add(new StudentOutDTO(student)));
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

    public PdfOutDTO getOffrePdfById(long id) throws NonExistentOfferExeption {
        @NotNull byte[] offer = getOffer(id).getPdf();
        String offreString = Arrays.toString(offer).replaceAll("\\s+", "");
        return PdfOutDTO.builder().pdf(offreString).build();
    }

    public List<StudentOutDTO> getUnvalidatedCVStudents() {
        List<StudentOutDTO> studentDTOS = new ArrayList<>();

        studentRepository.findAll().stream()
                .filter(student ->
                        student.getCvToValidate() != null &&
                                student.getCvToValidate().length > 0
                                && student.isConfirm()
                )
                .forEach(student -> {
                    StudentOutDTO dto = new StudentOutDTO(student);
                    dto.setPassword("");
                    dto.setCv("");

                    studentDTOS.add(dto);
                });

        return studentDTOS;
    }

    public StudentOutDTO validateStudentCV(long id) throws NonExistentEntityException, InvalidStatusException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(student.getId());
        if (cvStatusOpt.isEmpty()) {
            throw new NonExistentEntityException();
        }
        CvStatus cvStatus = cvStatusOpt.get();
        if (!cvStatus.getStatus().equals("PENDING")) {
            throw new InvalidStatusException();
        }
        cvStatus.setStatus("ACCEPTED");
        student.setCv(student.getCvToValidate());
        cvStatus.setRefusalMessage("");
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        cvStatusRepository.save(cvStatus);
        return new StudentOutDTO(student);
    }

    public StudentOutDTO removeStudentCvValidation(long id, String refusalReason) throws NonExistentEntityException, InvalidStatusException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(id);
        if (cvStatusOpt.isEmpty()) {
            throw new NonExistentEntityException();
        }
        CvStatus cvStatus = cvStatusOpt.get();
        if (!cvStatus.getStatus().equals("PENDING")) {
            throw new InvalidStatusException();
        }
        cvStatus.setStatus("REFUSED");
        cvStatus.setRefusalMessage(refusalReason);
        Student student = studentOpt.get();
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        return new StudentOutDTO(student);
    }

    public PdfOutDTO getStudentCvToValidate(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        byte[] cv = studentOpt.get().getCvToValidate();
        String cvConvert = Arrays.toString(cv).replaceAll("\\s+", "");
        return new PdfOutDTO(studentOpt.get().getId(), cvConvert);
    }

    private boolean deleteUnconfirmedGestionnaire(String email) throws NonExistentEntityException {
        Optional<Gestionnaire> studentOpt = gestionnaireRepository.findByEmail(email);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        Gestionnaire gestionnaire = studentOpt.get();
        if (currentTimestamp() - gestionnaire.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
            gestionnaireRepository.delete(gestionnaire);
            return true;
        }
        return false;
    }

    public boolean isGestionnaireInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedGestionnaire(email);
    }
}
