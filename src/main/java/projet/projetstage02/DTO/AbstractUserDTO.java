package projet.projetstage02.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
