package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.dto.*;
import projet.projetstage02.dto.applications.ApplicationDTO;
import projet.projetstage02.dto.applications.ApplicationListDTO;
import projet.projetstage02.dto.applications.RemoveApplicationDTO;
import projet.projetstage02.dto.cv.CvStatusDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.interview.InterviewSelectInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static projet.projetstage02.model.AbstractUser.Department;
import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final OffreRepository offreRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationAcceptationRepository applicationAcceptationRepository;
    private final StageContractRepository stageContractRepository;
    private final CvStatusRepository cvStatusRepository;
    private final InterviewRepository interviewRepository;

    private Student getStudentById(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return studentOpt.get();
    }

    private Offre getOfferById(long id) throws NonExistentEntityException {
        Optional<Offre> offerOpt = offreRepository.findById(id);
        if (offerOpt.isEmpty()) throw new NonExistentEntityException();
        return offerOpt.get();
    }

    public long saveStudent(StudentInDTO dto) {
        return studentRepository.save(dto.toModel()).getId();
    }

    public StudentOutDTO getStudentByIdDTO(Long id) throws NonExistentEntityException {
        return new StudentOutDTO(getStudentById(id));
    }

    public StudentOutDTO getStudentByEmailPassword(String email, String password) throws NonExistentEntityException {
        var studentOpt = studentRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        return new StudentOutDTO(studentOpt.get());
    }

    public StudentOutDTO uploadCurriculumVitae(PdfDTO dto) throws NonExistentEntityException {
        Student student = getStudentById(dto.getStudentId());
        student.setCvToValidate(dto.getPdf());
        saveStudent(new StudentInDTO(student));

        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(student.getId());
        CvStatus status;
        status = cvStatusOpt.orElseGet(() -> CvStatus.builder()
                .studentId(student.getId())
                .build());

        status.setStatus("PENDING");
        status.setRefusalMessage("");
        cvStatusRepository.save(status);
        return new StudentOutDTO(student);
    }

    public List<OffreOutDTO> getOffersByStudentDepartment(long id) throws NonExistentEntityException {
        Department department = getStudentById(id).getDepartment();

        List<OffreOutDTO> offers = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        offre.isValide()
                                && offre.getDepartment().equals(department)
                )
                .forEach(offre -> {
                    OffreOutDTO dto = new OffreOutDTO(offre);
                    dto.setPdf(byteToString(new byte[0]));
                    offers.add(dto);
                });

        return offers;
    }

    public PdfOutDTO getOfferPdfById(long id) throws NonExistentEntityException {
        Offre offre = getOfferById(id);

        String cv = byteToString(offre.getPdf());

        return new PdfOutDTO(offre.getId(), cv);
    }

    public ApplicationDTO createPostulation(long studentId, long offerId) throws NonExistentEntityException, AlreadyExistingPostulation {
        Student student = getStudentById(studentId);

        Offre offer = getOfferById(offerId);

        Optional<Application> postulationOpt = applicationRepository.findByStudentIdAndOfferId(studentId, offerId);
        if (postulationOpt.isPresent()) throw new AlreadyExistingPostulation();

        Application application = new Application(offer.getId(), student.getId());
        applicationRepository.save(application);

        return ApplicationDTO.builder()
                .studentId(student.getId())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .offerId(offer.getId())
                .company(offer.getNomDeCompagnie())
                .build();
    }

    public boolean isStudentInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedStudent(email);
    }

    private boolean isEmailUnique(String email) {
        return studentRepository.findByEmail(email).isEmpty();
    }

    private boolean deleteUnconfirmedStudent(String email) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        Student student = studentOpt.get();
        if (currentTimestamp() - student.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
            studentRepository.delete(student);
            return true;
        }
        return false;
    }

    public ApplicationListDTO getPostulsOfferId(long studentId) throws NonExistentEntityException {
        Student student = getStudentById(studentId);

        List<Long> offersId = new ArrayList<>();
        List<Long> removableOffersId = new ArrayList<>();
        applicationRepository.findByStudentId(studentId)
                .forEach(
                        application -> {
                            long offerId = application.getOfferId();
                            offersId.add(offerId);
                            if(canRemoveApplication(studentId, offerId))
                                removableOffersId.add(offerId);
                        }
                );

        return ApplicationListDTO.builder()
                .studentId(student.getId())
                .offersId(offersId)
                .removableOffersId(removableOffersId)
                .build();
    }

    private boolean canRemoveApplication(long studentId, long offerId) {
        Optional<StageContract> contractOpt = stageContractRepository.findByStudentIdAndOfferId(studentId, offerId);
        return contractOpt.isEmpty();
    }


    public CvStatusDTO getStudentCvStatus(long studentId) throws NonExistentEntityException {
        getStudentById(studentId);

        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(studentId);
        if (cvStatusOpt.isEmpty()) {
            CvStatus status = CvStatus.builder()
                    .status("NOTHING")
                    .refusalMessage("")
                    .studentId(studentId)
                    .build();

            cvStatusRepository.save(status);
            return new CvStatusDTO(status);
        }

        return new CvStatusDTO(cvStatusOpt.get());
    }

    public List<StageContractOutDTO> getContracts(long studentId, String session) throws NonExistentEntityException {
        getStudentById(studentId);

        List<StageContractOutDTO> contracts = new ArrayList<>();

        List<StageContract> all = stageContractRepository.findByStudentId(studentId);
        all.stream()
                .filter(stageContract -> stageContract.getSession().equals(session))
                .forEach(stageContract -> contracts.add(new StageContractOutDTO(stageContract)));

        return contracts;
    }

    public StageContractOutDTO addSignatureToContract(SignatureInDTO signature) throws NonExistentEntityException, InvalidOwnershipException {
        Student student = getStudentById(signature.getUserId());

        Optional<StageContract> stageContractOpt = stageContractRepository.findById(signature.getContractId());
        if (stageContractOpt.isEmpty()) throw new NonExistentEntityException();
        StageContract stageContract = stageContractOpt.get();

        if (student.getId() != stageContract.getStudentId())
            throw new InvalidOwnershipException();

        stageContract.setStudentSignature(signature.getSignature());
        stageContract.setStudentSignatureDate(LocalDateTime.now());
        stageContractRepository.save(stageContract);

        return new StageContractOutDTO(stageContract);
    }

    public InterviewOutDTO selectInterviewTime(InterviewSelectInDTO interviewDTO)
            throws NonExistentEntityException, InvalidOwnershipException, InvalidDateFormatException, InvalidDateException {
        Student student = getStudentById(interviewDTO.getStudentId());

        Optional<Interview> interviewOpt = interviewRepository.findById(interviewDTO.getInterviewId());
        if(interviewOpt.isEmpty()) throw new NonExistentEntityException();
        Interview interview = interviewOpt.get();

        if(interview.getStudentId() != student.getId()) throw new InvalidOwnershipException();

        try {
            LocalDateTime dateTime = LocalDateTime.parse(interviewDTO.getSelectedDate());
            if(!interview.getCompanyDateOffers().contains(dateTime)) throw new InvalidDateException();
            interview.setStudentSelectedDate(dateTime);
        } catch (InvalidDateException e){
          throw new InvalidDateException();
        } catch (Exception e){
            throw new InvalidDateFormatException();
        }

        interviewRepository.save(interview);

        return new InterviewOutDTO(interview);
    }

    public List<InterviewOutDTO> getInterviews(long studentId) throws NonExistentEntityException {
        getStudentById(studentId);

        List<InterviewOutDTO> interviews = new ArrayList<>();

        interviewRepository.findByStudentId(studentId)
                .stream()
                .forEach(
                        interview -> interviews.add(new InterviewOutDTO(interview))
                );

        return interviews;
    }

    public ApplicationListDTO removeApplication (RemoveApplicationDTO dto)
            throws NonExistentEntityException, CantRemoveApplicationException {
        Student student = getStudentById(dto.getStudentId());

        Offre offer = getOfferById(dto.getOfferId());

        Optional<Application> applicationOpt = applicationRepository.findByStudentIdAndOfferId(student.getId(), offer.getId());
        if(applicationOpt.isEmpty()) throw new NonExistentEntityException();
        Application application = applicationOpt.get();

        Optional<StageContract> contract
                = stageContractRepository.findByStudentIdAndOfferId(student.getId(), offer.getId());
        if(contract.isPresent()) throw new CantRemoveApplicationException();

        Optional<Interview> interviewOpt
                = interviewRepository.findByStudentIdAndOfferId(student.getId(), offer.getId());
        if(interviewOpt.isPresent()){
            Interview interview = interviewOpt.get();
            interviewRepository.delete(interview);
        }

        Optional<ApplicationAcceptation> applicationAcceptationOpt
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offer.getId(), student.getId());
        if(applicationAcceptationOpt.isPresent()){
            ApplicationAcceptation applicationAcceptation = applicationAcceptationOpt.get();
            applicationAcceptationRepository.delete(applicationAcceptation);
        }

        applicationRepository.delete(application);

        return getPostulsOfferId(student.getId());
    }
}
