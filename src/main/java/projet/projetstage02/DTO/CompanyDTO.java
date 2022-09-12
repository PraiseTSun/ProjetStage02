package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class CompanyDTO extends AbstractUserDTO<Company>{
    private String department;
    private String name;

    public CompanyDTO(String firstName, String lastName, String email, String password, Boolean isConfirmed, String department, String name) {
        super( "0",firstName, lastName, email, password, isConfirmed);
        this.department = department;
        this.name = name;
    }

    public CompanyDTO(String department, String name) {
        this.department = department;
        this.name = name;
    }

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
    public Company getOrigin() {
        Company company = new Company(firstName, lastName, email, password, AbstractUser.Department.valueOf(department), name);
        company.setId(Long.parseLong(id));
        return company;
    }
}
