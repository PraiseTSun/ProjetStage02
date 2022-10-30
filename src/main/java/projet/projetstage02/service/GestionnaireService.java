package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final StageContractRepository stageContractRepository;
    private final ApplicationAcceptationRepository applicationAcceptationRepository;

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

    public List<OffreDTO> getUnvalidatedOffers() {
        List<OffreDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        !offre.isValide() && isRightSession(offre.getSession(), getNextYear()))
                .forEach(offre ->
                        offres.add(new OffreDTO(offre)));
        offres.forEach(offre -> offre.setPdf(new byte[0]));
        return offres;
    }

    public List<OffreDTO> getValidatedOffers(int year) {
        List<OffreDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        offre.isValide() && isRightSession(offre.getSession(), year))
                .forEach(offre ->
                        offres.add(new OffreDTO(offre)));
        offres.forEach(offre -> offre.setPdf(new byte[0]));
        return offres;
    }


    public OffreDTO validateOfferById(Long id) throws NonExistentOfferExeption, ExpiredSessionException {

        Offre offre = getOffer(id);
        if (!isRightSession(offre.getSession(), getNextYear())) {
            throw new ExpiredSessionException();
        }
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

    public PdfOutDTO getOffrePdfById(long id) throws NonExistentOfferExeption {
        @NotNull byte[] offer = getOffer(id).getPdf();
        String offreString = byteToString(offer);
        return PdfOutDTO.builder().pdf(offreString).build();
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

    public StudentDTO validateStudentCV(long id) throws NonExistentEntityException, InvalidStatusException {
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
        return new StudentDTO(student);
    }

    public StudentDTO removeStudentCvValidation(long id, String refusalReason) throws NonExistentEntityException, InvalidStatusException {
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
        return new StudentDTO(student);
    }

    public PdfOutDTO getStudentCvToValidate(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        byte[] cv = studentOpt.get().getCvToValidate();
        String cvConvert = byteToString(cv);
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

    public StageContractOutDTO createStageContract(StageContractInDTO contract)
            throws NonExistentEntityException, NonExistentOfferExeption, AlreadyExistingStageContractException {
        Optional<Student> studentOpt = studentRepository.findById(contract.getStudentId());
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();

        Optional<Offre> offerOpt = offreRepository.findById(contract.getOfferId());
        if (offerOpt.isEmpty()) throw new NonExistentOfferExeption();
        Offre offer = offerOpt.get();

        Optional<Company> companyOpt = companyRepository.findById(offer.getIdCompagnie());
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();
        Company company = companyOpt.get();

        String description = company.getCompanyName() + " a accepté " + student.getFirstName() + " "
                + student.getLastName() + " en tant que " + offer.getPosition() + " pour la session "
                + offer.getSession() + ".";

        Optional<StageContract> stageContractOpt
                = stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(student.getId(), company.getId(), offer.getId());
        if (stageContractOpt.isPresent()) throw new AlreadyExistingStageContractException();

        StageContract stageContract = StageContract.builder()
                .studentId(student.getId())
                .offerId(offer.getId())
                .companyId(company.getId())
                .session(offer.getSession())
                .description(description)
                .companySignature(new byte[0])
                .build();

        stageContractRepository.save(stageContract);
        Optional<ApplicationAcceptation> application
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offer.getId(), student.getId());
        applicationAcceptationRepository.delete(application.get());

        stageContractOpt
                = stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(student.getId(), company.getId(), offer.getId());

        return new StageContractOutDTO(stageContractOpt.get());
    }

    public UnvalidatedAcceptationsDTO getUnvalidatedAcceptation() {
        UnvalidatedAcceptationsDTO unvalidatedAcceptationsDTO = new UnvalidatedAcceptationsDTO();

        List<ApplicationAcceptation> applications = applicationAcceptationRepository.findAll();
        applications.stream()
                .forEach(applicationAcceptation -> {
                    Optional<Offre> offerOpt = offreRepository.findById(applicationAcceptation.getOfferId());
                    Offre offer = offerOpt.get();

                    Optional<Company> companyOpt = companyRepository.findById(offer.getIdCompagnie());
                    Company company = companyOpt.get();

                    Optional<Student> studentOpt = studentRepository.findById(applicationAcceptation.getStudentId());
                    Student student = studentOpt.get();

                    unvalidatedAcceptationsDTO.add(
                            UnvalidatedAcceptationDTO.builder()
                                    .employFullName(company.getFirstName() + " " + company.getLastName())
                                    .companyName(company.getCompanyName())
                                    .studentId(student.getId())
                                    .studentFullName(student.getFirstName() + " " + student.getLastName())
                                    .offerId(offer.getId())
                                    .position(offer.getPosition())
                                    .build()
                    );
                });

        return unvalidatedAcceptationsDTO;
    }
}
