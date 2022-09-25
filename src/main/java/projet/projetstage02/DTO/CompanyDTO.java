package projet.projetstage02.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Company;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder()
@ToString(callSuper = true)
public class CompanyDTO extends AbstractUserDTO<Company> {
    private String department;
    private String companyName;

    public CompanyDTO(Company company) {
        id = company.getId();
        firstName = company.getFirstName();
        lastName = company.getLastName();
        email = company.getEmail();
        password = company.getPassword();
        isConfirmed = company.isConfirm();
        department = company.getDepartment().departement;
        companyName = company.getCompanyName();
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
                Department.getDepartment(department),
                companyName,
                inscriptionTimestamp,
                emailConfirmed);
        company.setId(id);
        return company;
    }
}
