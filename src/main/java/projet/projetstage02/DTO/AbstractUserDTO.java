package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
//todo reorganize dtos in different folders

public abstract class AbstractUserDTO<T> {
    protected long id = 0;
    @NotBlank
    @Size(min = 2)
    protected String firstName;
    @NotBlank
    @Size(min = 2)
    protected String lastName;
    @NotBlank
    @Email
    protected String email;
    @NotBlank
    @Size(min = 8)
    protected String password;
    protected boolean isConfirmed;
    protected long inscriptionTimestamp;
    protected boolean emailConfirmed;


    public abstract T toModel();
}
