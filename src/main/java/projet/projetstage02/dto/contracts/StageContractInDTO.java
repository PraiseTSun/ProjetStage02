package projet.projetstage02.dto.contracts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageContractInDTO {
    private String token;
    private long studentId;
    private long offerId;
}
