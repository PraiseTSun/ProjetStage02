package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.applications.ApplicationAcceptationDTO;
import projet.projetstage02.dto.applications.OfferApplicationDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.interview.CreateInterviewDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.notification.CompanyNotificationDTO;
import projet.projetstage02.dto.offres.OfferAcceptedStudentsDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static projet.projetstage02.utils.TimeUtil.*;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final OffreRepository offreRepository;
    private final StudentRepository studentRepository;
    private final ApplicationAcceptationRepository applicationAcceptationRepository;
    private final ApplicationRepository applicationRepository;
    private final StageContractRepository stageContractRepository;
    private final InterviewRepository interviewRepository;
    private final EvaluationEtudiantRepository evaluationEtudiantRepository;

    public long createOffre(OffreInDTO offreInDTO) {
        Offre offre = Offre.builder()
                .nomDeCompagnie(offreInDTO.getNomDeCompagnie())
                .idCompagnie(offreInDTO.getCompanyId())
                .department(Department.getDepartment(offreInDTO.getDepartment()))
                .position(offreInDTO.getPosition())
                .heureParSemaine(offreInDTO.getHeureParSemaine())
                .dateStageDebut(offreInDTO.getDateStageDebut())
                .dateStageFin(offreInDTO.getDateStageFin())
                .salaire(offreInDTO.getSalaire())
                .session(offreInDTO.getSession())
                .adresse(offreInDTO.getAdresse())
                .pdf(offreInDTO.getPdf())
                .build();

        return offreRepository.save(offre).getId();
    }

    public long saveCompany(CompanyDTO dto) {
        return companyRepository.save(dto.toModel()).getId();
    }

    public boolean isCompanyInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedCompany(email);
    }

    private boolean isEmailUnique(String email) {
        return companyRepository.findByEmail(email).isEmpty();
    }

    private boolean deleteUnconfirmedCompany(String email) throws NonExistentEntityException {
        Optional<Company> companyOpt = companyRepository.findByEmail(email);
        if (companyOpt.isEmpty())
            throw new NonExistentEntityException();
        Company company = companyOpt.get();
        if (!company.isEmailConfirmed() && currentTimestamp() - company.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
            companyRepository.delete(company);
            return true;
        }
        return false;
    }

    private Student getStudentById(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return studentOpt.get();
    }


    private Company getCompanyById(long id) throws NonExistentEntityException {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();
        return companyOpt.get();
    }

    public CompanyDTO getCompanyByIdDTO(long id) throws NonExistentEntityException {
        return new CompanyDTO(getCompanyById(id));
    }

    private Offre getOfferById(long id) throws NonExistentEntityException {
        Optional<Offre> offerOpt = offreRepository.findById(id);
        if (offerOpt.isEmpty()) throw new NonExistentEntityException();
        return offerOpt.get();
    }

    public ApplicationAcceptationDTO saveStudentApplicationAccepted(long offerId, long studentId) throws NonExistentEntityException, NonExistentOfferExeption, AlreadyExistingAcceptationException {
        Student student = getStudentById(studentId);

        Offre offre = getOfferById(offerId);

        Optional<ApplicationAcceptation> applicationOpt
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offerId, studentId);
        if (applicationOpt.isPresent()) throw new AlreadyExistingAcceptationException();

        ApplicationAcceptation application = ApplicationAcceptation.builder()
                .studentId(student.getId())
                .studentName(student.getLastName() + " " + student.getFirstName())
                .offerId(offre.getId())
                .companyName(offre.getNomDeCompagnie())
                .build();
        applicationAcceptationRepository.save(application);

        applicationOpt = applicationAcceptationRepository.findByOfferIdAndStudentId(offerId, studentId);
        if (applicationOpt.isEmpty()) throw new NonExistentEntityException();
        return new ApplicationAcceptationDTO(applicationOpt.get());
    }

    public OfferAcceptedStudentsDTO getAcceptedStudentsForOffer(long offerId) throws NonExistentEntityException {
        Offre offre = getOfferById(offerId);

        List<Long> studentsId = new ArrayList<>();

        applicationAcceptationRepository.findAll()
                .stream()
                .filter(application -> application.getOfferId() == offre.getId())
                .forEach(application -> studentsId.add(application.getStudentId()));

        return OfferAcceptedStudentsDTO.builder()
                .offerId(offre.getId())
                .studentsId(studentsId)
                .build();
    }

    public StageContractOutDTO addSignatureToContract(SignatureInDTO signature) throws NonExistentEntityException, InvalidOwnershipException {
        Company company = getCompanyById(signature.getUserId());

        Optional<StageContract> stageContractOpt = stageContractRepository.findById(signature.getContractId());
        if (stageContractOpt.isEmpty()) throw new NonExistentEntityException();

        StageContract stageContract = stageContractOpt.get();

        if (company.getId() != stageContract.getCompanyId())
            throw new InvalidOwnershipException();

        stageContract.setCompanySignature(signature.getSignature());
        stageContract.setCompanySignatureDate(LocalDateTime.now());
        stageContractRepository.save(stageContract);

        return new StageContractOutDTO(stageContract);
    }

    public OfferApplicationDTO getStudentsForOffer(long offerId) throws NonExistentEntityException {
        Offre offre = getOfferById(offerId);

        Company company = getCompanyById(offre.getIdCompagnie());

        List<StudentOutDTO> studentDTOS = new ArrayList<>();
        applicationRepository.findByOfferId(offerId).stream()
                .map(Application::getStudentId)
                .forEach(id -> {
                    Optional<Student> studentOpt = studentRepository.findById(id);
                    if (studentOpt.isEmpty()) return;
                    studentDTOS.add(new StudentOutDTO(studentOpt.get()));
                });

        List<StudentOutDTO> toReturn = studentDTOS.stream()
                .filter(student -> stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(student.getId(), company.getId(), offerId).isEmpty())
                .toList();
        return new OfferApplicationDTO(toReturn);
    }


    public PdfOutDTO getStudentCv(long studentId) throws NonExistentEntityException {
        Student student = getStudentById(studentId);

        byte[] cv = student.getCv();
        String cvConvert = Arrays.toString(cv).replaceAll("\\s+", "");
        return new PdfOutDTO(student.getId(), cvConvert);
    }


    public List<OffreOutDTO> getValidatedOffers(long id) {
        List<OffreOutDTO> offres = new ArrayList<>();
        offreRepository.findAllByIdCompagnie(id).stream().
                filter(offre ->
                        offre.isValide() && isRightSession(offre.getSession(), getNextYear()))
                .forEach(offre ->
                        offres.add(new OffreOutDTO(offre)));
        offres.forEach(offre -> offre.setPdf("[]"));
        return offres;
    }


    public ContractsDTO getContracts(long companyId, String session) throws NonExistentEntityException {
        getCompanyById(companyId);

        List<StageContractOutDTO> contracts = new ArrayList<>();

        stageContractRepository.findByCompanyId(companyId).stream()
                .filter(stageContract -> stageContract.getSession().equals(session))
                .forEach(stageContract -> contracts.add(new StageContractOutDTO(stageContract)));

        return ContractsDTO.builder().contracts(contracts).build();
    }

    public InterviewOutDTO createInterview(CreateInterviewDTO interviewDTO)
            throws NonExistentEntityException, InvalidDateFormatException, InvalidOwnershipException {
        Company company = getCompanyById(interviewDTO.getCompanyId());

        Student student = getStudentById(interviewDTO.getStudentId());

        Offre offer = getOfferById(interviewDTO.getOfferId());

        if (offer.getIdCompagnie() != company.getId()) throw new InvalidOwnershipException();

        List<LocalDateTime> dates = new ArrayList<>();

        for (String dateInt : interviewDTO.getCompanyDateOffers()) {
            try {
                dates.add(LocalDateTime.parse(dateInt));
            } catch (Exception e) {
                throw new InvalidDateFormatException();
            }
        }

        Interview interview = Interview.builder()
                .companyId(company.getId())
                .offerId(offer.getId())
                .studentId(student.getId())
                .companyDateOffers(dates)
                .studentSelectedDate(null)
                .build();
        interview = interviewRepository.save(interview);

        return new InterviewOutDTO(interview);
    }

    public List<InterviewOutDTO> getInterviews(long companyId) throws NonExistentEntityException {
        List<InterviewOutDTO> interviews = new ArrayList<>();

        getCompanyById(companyId);

        interviewRepository.findByCompanyId(companyId)
                .stream()
                .forEach(
                        interview -> interviews.add(new InterviewOutDTO(interview))
                );

        return interviews;
    }

    public void evaluateStudent(EvaluationEtudiantInDTO studentEvaluationInDTO) {
        evaluationEtudiantRepository.save(new EvaluationEtudiant(studentEvaluationInDTO));
    }

    public List<Long> getEvaluatedStudentsContracts(long companyId) {
        return stageContractRepository.findByCompanyId(companyId).stream().filter(stageContract -> {
            Optional<EvaluationEtudiant> opt = evaluationEtudiantRepository.findByContractId(stageContract.getId());
            return opt.isPresent();
        }).map(StageContract::getId).toList();
    }

    public CompanyNotificationDTO getNotification(long companyId) throws NonExistentEntityException {
        getCompanyById(companyId);

        return CompanyNotificationDTO.builder()
                .nbOffers(getOfferNotification(companyId))
                .nbContracts(getContractNotifications(companyId))
                .build();
    }

    private long getOfferNotification(long companyId) {
        List<Offre> offers = offreRepository.findAllByIdCompagnie(companyId);

        if(offers.size() == 0)
            return 0L;

        long nb = 0L;

        for(Offre offer : offers){
            long count = applicationRepository.findByOfferId(offer.getId())
                    .stream()
                    .filter(application -> applicationAcceptationRepository
                            .findByOfferIdAndStudentId(offer.getId(), application.getStudentId()).isEmpty())
                    .count();

            nb += count;
        }

        return nb;
    }

    private long getContractNotifications(long companyId) {
        return stageContractRepository.findByCompanyId(companyId)
                .stream()
                .filter(stageContract -> stageContract.getCompanySignature().isBlank())
                .count();
        }
}
