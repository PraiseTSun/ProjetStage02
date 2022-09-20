package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;
import projet.projetstage02.repository.CompanyRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CompanyService extends AbstractService<CompanyDTO> {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void saveCompany(String firstName, String lastName, String name, String email, String password,
            AbstractUser.Department department) {
        CompanyDTO dto = new CompanyDTO(
                firstName,
                lastName,
                email.toLowerCase(),
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime(),
                false,
                department.departement,
                name);
        saveCompany(dto);
    }

    public long saveCompany(CompanyDTO dto) {
        return companyRepository.save(dto.getClassOrigin()).getId();
    }

    @Override
    public boolean isUniqueEmail(String email) {
       Optional<Company> company = companyRepository.findByEmail(email);
        return company.isEmpty();
    }

    @Override
    public CompanyDTO getUserById(Long id) {
        var companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty())
            return null;
        return new CompanyDTO(companyOpt.get());
    }

    @Override
    public CompanyDTO getUserByEmailPassword(String email, String password) {
        var companyOpt = companyRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (companyOpt.isEmpty())
            return null;
        return new CompanyDTO(companyOpt.get());
    }
}