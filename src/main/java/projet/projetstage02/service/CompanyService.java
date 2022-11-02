package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.AlreadyExistingAcceptationException;
import projet.projetstage02.exception.InvalidOwnershipException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.sql.Timestamp;
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

    public long createOffre(OffreInDTO offreInDTO) {
        Offre offre = Offre.builder()
                .nomDeCompagnie(offreInDTO.getNomDeCompagnie())
                .idCompagnie(offreInDTO.getCompanyId())
                .department(Department.getDepartment(offreInDTO.getDepartment()))
                .position(offreInDTO.getPosition())
                .heureParSemaine(offreInDTO.getHeureParSemaine())
                .dateStage(offreInDTO.getDateStage())
                .salaire(offreInDTO.getSalaire())
                .session(offreInDTO.getSession())
                .adresse(offreInDTO.getAdresse())
                .pdf(offreInDTO.getPdf())
                .build();

        return offreRepository.save(offre).getId();
    }

    public void saveCompany(String firstName, String lastName, String name, String email, String password,
                            Department department) {
        CompanyDTO dto = CompanyDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email.toLowerCase())
                .password(password)
                .isConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .emailConfirmed(true)
                .department(department.departement)
                .companyName(name)
                .build();
        saveCompany(dto);
    }

    public long saveCompany(CompanyDTO dto) {
        return companyRepository.save(dto.toModel()).getId();
    }

    private boolean isEmailUnique(String email) {
        return companyRepository.findByEmail(email).isEmpty();
    }

    public CompanyDTO getCompanyById(Long id) throws NonExistentEntityException {
        var companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty())
            throw new NonExistentEntityException();
        return new CompanyDTO(companyOpt.get());
    }

    public CompanyDTO getCompanyByEmailPassword(String email, String password) throws NonExistentEntityException {
        var companyOpt = companyRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (companyOpt.isEmpty())
            throw new NonExistentEntityException();
        return new CompanyDTO(companyOpt.get());
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

    public boolean isCompanyInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedCompany(email);
    }

    public ApplicationAcceptationDTO saveStudentApplicationAccepted(long offerId, long studentId) throws NonExistentEntityException, NonExistentOfferExeption, AlreadyExistingAcceptationException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();

        Optional<Offre> offerOpt = offreRepository.findById(offerId);
        if (offerOpt.isEmpty()) throw new NonExistentOfferExeption();
        Offre offre = offerOpt.get();

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

    public OfferAcceptedStudentsDTO getAcceptedStudentsForOffer(long offerId) throws NonExistentOfferExeption {
        Optional<Offre> offreOpt = offreRepository.findById(offerId);
        if (offreOpt.isEmpty()) throw new NonExistentOfferExeption();
        Offre offre = offreOpt.get();

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
        Optional<Company> companyOpt = companyRepository.findById(signature.getUserId());
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<StageContract> stageContractOpt = stageContractRepository.findById(signature.getContractId());
        if (stageContractOpt.isEmpty()) throw new NonExistentEntityException();

        Company company = companyOpt.get();
        StageContract stageContract = stageContractOpt.get();

        if (company.getId() != stageContract.getCompanyId())
            throw new InvalidOwnershipException();

        stageContract.setCompanySignature(signature.getSignature());
        stageContract.setCompanySignatureDate(LocalDateTime.now());
        stageContractRepository.save(stageContract);

        return new StageContractOutDTO(stageContract);
    }

    public OfferApplicationDTO getStudentsForOffer(long offerId) throws NonExistentOfferExeption, NonExistentEntityException {
        Optional<Offre> optionalOffre = offreRepository.findById(offerId);
        if (optionalOffre.isEmpty()) throw new NonExistentOfferExeption();
        Offre offre = optionalOffre.get();
        Optional<Company> optionalCompany = companyRepository.findById(offre.getIdCompagnie());
        if (optionalCompany.isEmpty()) throw new NonExistentEntityException();
        Company company = optionalCompany.get();
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
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        byte[] cv = studentOpt.get().getCv();
        String cvConvert = Arrays.toString(cv).replaceAll("\\s+", "");
        return new PdfOutDTO(studentOpt.get().getId(), cvConvert);
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
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();

        List<StageContractOutDTO> contracts = new ArrayList<>();

        stageContractRepository.findByCompanyId(companyId).stream()
                .filter(stageContract -> stageContract.getSession().equals(session))
                .forEach(stageContract -> contracts.add(new StageContractOutDTO(stageContract)));

        return ContractsDTO.builder().applications(contracts).build();
    }
}
