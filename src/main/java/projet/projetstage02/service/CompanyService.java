package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.OffreRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final OffreRepository offreRepository;

    public long createOffre(OffreDTO offreDTO) {
        Offre offre = Offre.builder()
                .nomDeCompagie(offreDTO.getNomDeCompagnie())
                .department(Department.getDepartment(offreDTO.getDepartment()))
                .position(offreDTO.getPosition())
                .heureParSemaine(offreDTO.getHeureParSemaine())
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

    public boolean isEmailUnique(String email) {
        return companyRepository.findByEmail(email).isEmpty();
    }

    public CompanyDTO getCompanyById(Long id) throws NonExistentUserException {
        var companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty())
            throw new NonExistentUserException();
        return new CompanyDTO(companyOpt.get());
    }

    public CompanyDTO getCompanyByEmailPassword(String email, String password) throws NonExistentUserException {
        var companyOpt = companyRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (companyOpt.isEmpty())
            throw new NonExistentUserException();
        return new CompanyDTO(companyOpt.get());
    }
}
