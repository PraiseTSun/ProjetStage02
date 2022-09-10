package projet.projetstage02.DTO;

import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;

public class CompanyDTO extends AbstractUserDTO<Company>{
    private String department;
    private String name;

    public CompanyDTO(Company company){
        id = company.getId().toString();
        firstName = company.getFirstName();
        lastName = company.getLastName();
        email = company.getEmail();
        isConfirmed = company.getIsConfirm();
        department = company.getDepartment().toString();
        name = company.getName();
    }

    @Override
    public Company GetClass() {
        Company company = new Company(firstName, lastName, email, password, AbstractUser.Department.valueOf(department), name);
        company.setId(Long.parseLong(id));
        return company;
    }
}
