package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.OffreRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {
    //TODO when merging with token branch, user TimeUtil stuff
    private final long MILLI_SECOND_DAY = 864000000;
    private final CompanyRepository companyRepository;
    private final OffreRepository offreRepository;

    public long createOffre(OffreDTO offreDTO) {
        Offre offre = Offre.builder()
                .nomDeCompagnie(offreDTO.getNomDeCompagnie())
                .department(Department.getDepartment(offreDTO.getDepartment()))
                .position(offreDTO.getPosition())
                .heureParSemaine(offreDTO.getHeureParSemaine())
                .adresse(offreDTO.getAdresse())
                .pdf(offreDTO.getPdf())
                .build();

        return offreRepository.save(offre).getId();
    }
    public List<Offre> findOffre(){
        return offreRepository.findAll();
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

    public boolean isEmailUnique(String email) {
        return companyRepository.findByEmail(email).isEmpty();
    }

    public CompanyDTO getCompanyById(Long id) throws NonExistentEntityException {
        var companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();
        return new CompanyDTO(companyOpt.get());
    }

    public CompanyDTO getCompanyByEmailPassword(String email, String password) throws NonExistentEntityException {
        var companyOpt = companyRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();
        return new CompanyDTO(companyOpt.get());
    }
    public boolean deleteUnconfirmedCompany(CompanyDTO dto) throws NonExistentEntityException {
        Optional<Company> companyOpt = companyRepository.findByEmail(dto.getEmail());
        if(companyOpt.isEmpty()) throw new NonExistentEntityException();
        Company company = companyOpt.get();
        if(!company.isEmailConfirmed() && Timestamp.valueOf(LocalDateTime.now()).getTime() - company.getInscriptionTimestamp() > MILLI_SECOND_DAY){
            companyRepository.delete(company);
            return true;
        }
        return false;
    }
}
