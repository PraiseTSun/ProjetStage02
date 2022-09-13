package projet.projetstage02.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Student extends AbstractUser{
    private Department department;

    public Student(String firstName, String lastName, String email, String password, Department department) {
        super(firstName, lastName, email, password);
        this.department = department;
    }
}
