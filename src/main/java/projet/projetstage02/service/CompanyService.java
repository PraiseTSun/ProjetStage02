package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.ApplicationAcceptationDTO;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.exception.AlreadyExistingAcceptationException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.ApplicationAcceptation;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.ApplicationAcceptationRepository;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final OffreRepository offreRepository;
    private final StudentRepository studentRepository;
    private final ApplicationAcceptationRepository applicationAcceptationRepository;

    public long createOffre(OffreDTO offreDTO) {
        Offre offre = Offre.builder()
                .nomDeCompagnie(offreDTO.getNomDeCompagnie())
                .department(Department.getDepartment(offreDTO.getDepartment()))
                .position(offreDTO.getPosition())
                .heureParSemaine(offreDTO.getHeureParSemaine())
                .salaire(offreDTO.getSalaire())
                .adresse(offreDTO.getAdresse())
                .pdf(offreDTO.getPdf())
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

    public ApplicationAcceptationDTO saveStudentApplicationAccepted(long offerId, long studentId) throws Exception {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();

        Optional<Offre> offerOpt = offreRepository.findById(offerId);
        if(offerOpt.isEmpty()) throw new NonExistentOfferExeption();
        Offre offre = offerOpt.get();

        Optional<ApplicationAcceptation> applicationOpt
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offerId, studentId);
        if(applicationOpt.isPresent()) throw new AlreadyExistingAcceptationException();

        ApplicationAcceptation application = ApplicationAcceptation.builder()
                .studentId(student.getId())
                .studentName(student.getLastName() + " " + student.getFirstName())
                .offerId(offre.getId())
                .companyName(offre.getNomDeCompagnie())
                .build();
        applicationAcceptationRepository.save(application);

        applicationOpt = applicationAcceptationRepository.findByOfferIdAndStudentId(offerId, studentId);

        return new ApplicationAcceptationDTO(applicationOpt.get());
    }
}
