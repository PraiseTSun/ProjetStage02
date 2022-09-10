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

    public CompanyDTO createCompany(String firstName, String lastName, String name, String email, String password, AbstractUser.Department department) {
        Company company = new Company(firstName, lastName, email, password, department, name);
        companyRepository.save(company);
        return new CompanyDTO(company);
    }
}
