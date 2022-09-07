package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;

@Component
public class CompanyService {
    public Company createCompany(String firstName, String lastName, String name, String email, String password, AbstractUser.Department departement) {
        Company company = new Company(firstName, lastName, email, password, departement, name);
        return null;
    }
}
