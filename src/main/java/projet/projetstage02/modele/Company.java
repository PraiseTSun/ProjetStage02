package projet.projetstage02.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends AbstractUser{
    private  Department department;

    public Company(String firstName, String lastName, String email, String password, Department department) {
        super(firstName, lastName, email, password);
        this.department = department;
    }
}
