package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.repository.CompanyRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Component
public class CompanyService extends AbstractService<CompanyDTO>{
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void createCompany(String firstName, String lastName, String name, String email, String password, AbstractUser.Department department) {
        CompanyDTO dto = new CompanyDTO(
                firstName,
                lastName,
                email,
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime(),
                false,
                department.toString(),
                name);
        createCompany(dto);
    }

    public void createCompany(CompanyDTO dto) {
        companyRepository.save(dto.getOrigin());
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return false;
    }

    @Override
    public CompanyDTO getUserById(Long id) {
        var companyOpt = companyRepository.findById(id);
        if(companyOpt.isEmpty())
            return null;
        return new CompanyDTO(companyOpt.get());
    }
}
