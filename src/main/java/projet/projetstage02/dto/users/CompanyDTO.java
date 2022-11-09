package projet.projetstage02.dto.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Company;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder()
@ToString(callSuper = true)
public class CompanyDTO extends AbstractUserDTO<Company> {
    @NotBlank
    @Pattern(regexp = ("Techniques de linformatique|Techniques de la logistique du transport"))
    private String department;
    @NotBlank
    @Size(min = 2)
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
