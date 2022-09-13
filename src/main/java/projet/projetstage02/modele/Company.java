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
    private String name;

    public Company(String firstName, String lastName, String email, String password, Department department, String name) {
        super(firstName, lastName, email, password);
        this.department = department;
        this.name = name;
    }
}
