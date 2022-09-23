package projet.projetstage02.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Company;

@Data
@EqualsAndHashCode(callSuper = false)
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
        super(0L, firstName, lastName, email, password, isConfirmed, inscriptionTimestamp, emailConfirmed);
        this.department = department;
        this.name = name;
    }

    public CompanyDTO(String department, String name) {
        this.department = department;
        this.name = name;
    }

    public CompanyDTO(Company company) {
        id = company.getId();
        firstName = company.getFirstName();
        lastName = company.getLastName();
        email = company.getEmail();
        password = company.getPassword();
        isConfirmed = company.isConfirm();
        department = company.getDepartment().departement;
        name = company.getName();
        inscriptionTimestamp = company.getInscriptionTimestamp();
        emailConfirmed = company.isEmailConfirmed();
    }

    @Override
    public Company toModel() {
        Company company = new Company(
                firstName,
                lastName,
                email,
                password,
                AbstractUser.Department.getDepartment(department),
                name,
                inscriptionTimestamp,
                emailConfirmed);
        company.setId(id);
        return company;
    }
}
