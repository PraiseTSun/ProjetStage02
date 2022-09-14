package projet.projetstage02.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class CompanyDTO extends AbstractUserDTO<Company> {
    private String department;
    private String name;

    public CompanyDTO(String firstName,
                      String lastName,
                      String email,
                      String password,
                      boolean isConfirmed,
                      long inscriptionTimestamp,
                      boolean emailConfirmed,
                      String department,
                      String name) {
        super("0", firstName, lastName, email, password, isConfirmed, inscriptionTimestamp, emailConfirmed);
        this.department = department;
        this.name = name;
    }

    public CompanyDTO(String department, String name) {
        this.department = department;
        this.name = name;
    }

    public CompanyDTO(Company company) {
        id = String.valueOf(company.getId());
        firstName = company.getFirstName();
        lastName = company.getLastName();
        email = company.getEmail();
        isConfirmed = company.isConfirm();
        department = company.getDepartment().toString();
        name = company.getName();
        inscriptionTimestamp = company.getInscriptionTimestamp();
        emailConfirmed = company.isEmailConfirmed();
    }

    @Override
    public Company getOrigin() {
        Company company = new Company(
                firstName,
                lastName,
                email,
                password,
                AbstractUser.Department.valueOf(department),
                name,
                inscriptionTimestamp,
                emailConfirmed);
        company.setId(Long.parseLong(id));
        return company;
    }
}
