package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UvalidatedAcceptationDTO {
    private String employFullName;
    private String companyName;
    private long studentId;
    private String studentFullName;
    private long offerId;
    private String position;
}
