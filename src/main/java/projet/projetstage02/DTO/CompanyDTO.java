package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO extends AbstractUserDTO{
    private String departement;
}
