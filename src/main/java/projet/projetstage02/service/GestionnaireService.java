package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInfoDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import javax.validation.constraints.NotNull;
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

    private final ApplicationRepository applicationRepository;
    private final EvaluationRepository evaluationRepository;

    public long saveGestionnaire(GestionnaireDTO dto) {
        return gestionnaireRepository.save(dto.toModel()).getId();
    }

    public GestionnaireDTO getGestionnaireById(Long id) throws NonExistentEntityException {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public void validateCompany(Long id) throws NonExistentEntityException {
        Company company = getCompanyById(id);
        company.setConfirm(true);
        companyRepository.save(company);
    }

    public void validateStudent(Long id) throws NonExistentEntityException {
        Student student = getStudentById(id);
        student.setConfirm(true);
        studentRepository.save(student);
    }

    public void removeCompany(long id) throws NonExistentEntityException {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    public void removeStudent(long id) throws NonExistentEntityException {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    private Company getCompanyById(long id) throws NonExistentEntityException {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) throw new NonExistentEntityException();
        return companyOptional.get();
    }

    private Student getStudentById(long id) throws NonExistentEntityException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) throw new NonExistentEntityException();
        return studentOptional.get();
    }

    private Offre getOfferById(long id) throws NonExistentOfferExeption {
        Optional<Offre> offreOpt = offreRepository.findById(id);
        if (offreOpt.isEmpty()) throw new NonExistentOfferExeption();
        return offreOpt.get();
    }

    private CvStatus getCvStatusOptById(long id) throws NonExistentEntityException {
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(id);
        if (cvStatusOpt.isEmpty()) throw new NonExistentEntityException();
        return cvStatusOpt.get();
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

    public OffreOutDTO validateOfferById(Long id) throws NonExistentOfferExeption, ExpiredSessionException {

        Offre offre = getOfferById(id);
        if (!isRightSession(offre.getSession(), getNextYear())) {
            throw new ExpiredSessionException();
        }
        offre.setValide(true);
        offreRepository.save(offre);

        return new OffreOutDTO(offre);
    }

    public void removeOfferById(long id) throws NonExistentOfferExeption {
        offreRepository.delete(getOfferById(id));
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
        @NotNull byte[] offer = getOfferById(id).getPdf();
        String offreString = byteToString(offer);
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
        Student student = getStudentById(id);

        CvStatus cvStatus = getCvStatusOptById(student.getId());

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
        Student student = getStudentById(id);

        CvStatus cvStatus = getCvStatusOptById(id);

        if (!cvStatus.getStatus().equals("PENDING")) {
            throw new InvalidStatusException();
        }
        cvStatus.setStatus("REFUSED");
        cvStatus.setRefusalMessage(refusalReason);

        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);

        return new StudentOutDTO(student);
    }

    public PdfOutDTO getStudentCvToValidate(long studentId) throws NonExistentEntityException {
        Student student = getStudentById(studentId);

        byte[] cv = student.getCvToValidate();
        String cvConvert = byteToString(cv);
        return new PdfOutDTO(student.getId(), cvConvert);
    }

    public boolean isGestionnaireInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedGestionnaire(email);
    }

    private boolean isEmailUnique(String email) {
        return gestionnaireRepository.findByEmail(email).isEmpty();
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

    public StageContractOutDTO createStageContract(StageContractInDTO contract)
            throws NonExistentEntityException, NonExistentOfferExeption, AlreadyExistingStageContractException {
        Student student = getStudentById(contract.getStudentId());

        Offre offer = getOfferById(contract.getOfferId());

        Company company = getCompanyById(offer.getIdCompagnie());

        Optional<StageContract> stageContractOpt
                = stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(student.getId(), company.getId(), offer.getId());
        if (stageContractOpt.isPresent()) throw new AlreadyExistingStageContractException();

        StageContract stageContract = StageContract.builder()
                .studentId(student.getId())
                .offerId(offer.getId())
                .companyId(company.getId())
                .session(offer.getSession())
                .companySignature("")
                .studentSignature("")
                .description(getContractDescription(student, offer, company))
                .build();

        Optional<ApplicationAcceptation> application
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offer.getId(), student.getId());
        if (application.isEmpty()) throw new NonExistentEntityException();
        applicationAcceptationRepository.delete(application.get());
        stageContract = stageContractRepository.save(stageContract);
        return new StageContractOutDTO(stageContract);
    }

    private String getContractDescription(Student student, Offre offer, Company company) {
        return company.getCompanyName() + " a accepté " + student.getFirstName() + " "
                + student.getLastName() + " Pour le role de " + offer.getPosition() + " pour la session "
                + offer.getSession() + ".";
    }

    public ContractsDTO getContractsToCreate() {
        ContractsDTO contractsDTO = new ContractsDTO();

        List<ApplicationAcceptation> all = applicationAcceptationRepository.findAll();
        all
                .forEach(application -> {

                    Optional<Offre> offerOpt = offreRepository.findById(application.getOfferId());
                    if (offerOpt.isEmpty()) return;
                    Offre offer = offerOpt.get();
                    Optional<Company> companyOpt = companyRepository.findById(offer.getIdCompagnie());
                    if (companyOpt.isEmpty()) return;
                    Optional<Student> studentOpt = studentRepository.findById(application.getStudentId());
                    if (studentOpt.isEmpty()) return;

                    Student student = studentOpt.get();
                    Company company = companyOpt.get();
                    Offre offre = offerOpt.get();

                    StageContractOutDTO stageContractOutDTO = StageContractOutDTO.builder()
                            .companyId(company.getId())
                            .offerId(offre.getId())
                            .description(getContractDescription(student, offre, company))
                            .companyName(company.getCompanyName())
                            .position(offre.getPosition())
                            .employFullName(company.getFirstName() + " " + company.getLastName())
                            .studentId(student.getId())
                            .studentFullName(student.getFirstName() + " " + student.getLastName())
                            .build();

                    stageContractOutDTO.setEmployFullName(company.getFirstName() + " " + company.getLastName());
                    stageContractOutDTO.setStudentFullName(student.getFirstName() + " " + student.getLastName());
                    stageContractOutDTO.setPosition(offre.getPosition());
                    stageContractOutDTO.setCompanyName(company.getCompanyName());

                    contractsDTO.add(stageContractOutDTO);
                });
        return contractsDTO;
    }


    public MillieuStageEvaluationInfoDTO getMillieuEvaluationInfoForContract(long contractId) throws NonExistentOfferExeption, NonExistentEntityException {
        Optional<StageContract> optional = stageContractRepository.findById(contractId);
        if (optional.isEmpty()) {
            throw new NonExistentEntityException();
        }
        StageContract stageContract = optional.get();

        Offre offre = getOfferById(stageContract.getOfferId());

        Student student = getStudentById(stageContract.getStudentId());

        Company company = getCompanyById(stageContract.getCompanyId());

        return new MillieuStageEvaluationInfoDTO(company, offre, student);
    }

    public void saveEvaluateStage(MillieuStageEvaluationInDTO millieuStageEvaluationInDTO) {
        evaluationRepository.save(new Evaluation(millieuStageEvaluationInDTO));
    }

    public ContractsDTO getContracts() {
        List<StageContractOutDTO> contracts = new ArrayList<>();
        stageContractRepository.findAll().forEach(stageContract -> contracts.add(new StageContractOutDTO(stageContract)));
        return ContractsDTO.builder().contracts(contracts).build();
    }
}
