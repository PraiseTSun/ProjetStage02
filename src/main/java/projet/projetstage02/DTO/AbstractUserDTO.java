package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractUserDTO<T> {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected Boolean isConfirmed;

    public abstract T getOrigin();
}
