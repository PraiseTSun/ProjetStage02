package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.AlreadyExistingPostulation;
import projet.projetstage02.exception.InvalidOwnershipException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final StageContractRepository stageContractRepository;
    private final CvStatusRepository cvStatusRepository;

    public void saveStudent(String firstName,
                            String lastName,
                            String email,
                            String password,
                            Department department) {
        StudentDTO dto = StudentDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email.toLowerCase())
                .password(password)
                .department(department.departement)
                .isConfirmed(false)
                .emailConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        saveStudent(dto);
    }

    public long saveStudent(StudentDTO dto) {
        return studentRepository.save(dto.toModel()).getId();
    }

    private boolean isEmailUnique(String email) {
        return studentRepository.findByEmail(email).isEmpty();
    }

    public StudentDTO getStudentById(Long id) throws NonExistentEntityException {
        var studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO getStudentByEmailPassword(String email, String password) throws NonExistentEntityException {
        var studentOpt = studentRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO uploadCurriculumVitae(PdfDTO dto) throws NonExistentEntityException {
        Student student = getStudentById(dto.getStudentId()).toModel();
        student.setCvToValidate(dto.getPdf());
        saveStudent(new StudentDTO(student));
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(student.getId());
        CvStatus status;
        status = cvStatusOpt.orElseGet(() -> CvStatus.builder()
                .studentId(student.getId())
                .build());

        status.setStatus("PENDING");
        status.setRefusalMessage("");
        cvStatusRepository.save(status);
        return new StudentDTO(student);
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

    public List<OffreDTO> getOffersByStudentDepartment(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        Department department = studentOpt.get().getDepartment();

        List<OffreDTO> offers = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        offre.isValide()
                                && offre.getDepartment().equals(department)
                )
                .forEach(offre -> {
                    OffreDTO dto = new OffreDTO(offre);
                    dto.setPdf(new byte[0]);
                    offers.add(dto);
                });

        return offers;
    }

    public PdfOutDTO getOfferPdfById(long id) throws NonExistentEntityException {
        Optional<Offre> offerOpt = offreRepository.findById(id);
        if (offerOpt.isEmpty())
            throw new NonExistentEntityException();
        Offre offre = offerOpt.get();
        String cv = byteToString(offre.getPdf());
        return new PdfOutDTO(offre.getId(), cv);
    }

    public ApplicationDTO createPostulation(long studentId, long offerID) throws NonExistentEntityException, AlreadyExistingPostulation {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<Offre> offerOpt = offreRepository.findById(offerID);
        if (offerOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<Application> postulationOpt = applicationRepository.findByStudentIdAndOfferId(studentId, offerID);
        if (postulationOpt.isPresent()) throw new AlreadyExistingPostulation();

        Student student = studentOpt.get();
        Offre offer = offerOpt.get();

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

    public ApplicationListDTO getPostulsOfferId(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();

        List<Long> offersId = new ArrayList<>();
        applicationRepository.findByStudentId(studentId)
                .forEach(
                        application -> offersId.add(application.getOfferId())
                );

        return ApplicationListDTO.builder()
                .studentId(studentOpt.get().getId())
                .offersId(offersId)
                .build();
    }

    public CvStatusDTO getStudentCvStatus(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new NonExistentEntityException();
        }
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
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();

        List<StageContractOutDTO> contracts = new ArrayList<>();

        List<StageContract> all = stageContractRepository.findByStudentId(studentId);
        all.stream()
                .filter(stageContract -> stageContract.getSession().equals(session))
                .forEach(stageContract -> contracts.add(new StageContractOutDTO(stageContract)));

        return contracts;
    }

    public StageContractOutDTO addSignatureToContract(SignatureInDTO signature) throws NonExistentEntityException, InvalidOwnershipException {
        Optional<Student> studentOpt = studentRepository.findById(signature.getUserId());
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<StageContract> stageContractOpt = stageContractRepository.findById(signature.getContractId());
        if (stageContractOpt.isEmpty()) throw new NonExistentEntityException();

        Student student = studentOpt.get();
        StageContract stageContract = stageContractOpt.get();

        if(student.getId() != stageContract.getStudentId())
            throw new InvalidOwnershipException();

        stageContract.setStudentSignature(signature.getSignature());
        stageContract.setStudentSignatureDate(LocalDateTime.now());
        stageContractRepository.save(stageContract);

        return new StageContractOutDTO(stageContract);
    }
}
