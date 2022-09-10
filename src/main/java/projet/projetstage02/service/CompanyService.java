package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;
import projet.projetstage02.repository.CompanyRepository;

@Component
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void createCompany(String firstName, String lastName, String name, String email, String password, AbstractUser.Department department) {
        CompanyDTO dto = new CompanyDTO(firstName, lastName, email, password, false, department.toString(), name);
        createCompany(dto);
    }

    public void createCompany(CompanyDTO dto) {
        companyRepository.save(dto.getOrigin());
    }
}
