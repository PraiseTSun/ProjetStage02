package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractUserDTO {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected Boolean isConfirmed;
}
