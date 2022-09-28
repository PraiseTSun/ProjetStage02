package projet.projetstage02.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token{
    @Id
    private String token;
    private long userId;
    private UserTypes userType;

    public enum UserTypes{
        STUDENT,
        COMPANY,
        GESTIONNAIRE
    }
}
