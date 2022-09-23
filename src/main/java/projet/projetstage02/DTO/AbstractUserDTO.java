package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractUserDTO<T> {
    protected long id = 0;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected boolean isConfirmed;
    protected long inscriptionTimestamp;
    protected boolean emailConfirmed;


    public abstract T toModel();
}
