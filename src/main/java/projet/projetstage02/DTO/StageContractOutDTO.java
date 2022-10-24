package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.StageContract;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageContractOutDTO {
    private long studentId;
    private long offerId;
    private long companyId;
    private String description;

    public StageContractOutDTO (StageContract contact){
        studentId = contact.getStudentId();
        offerId = contact.getOfferId();
        companyId = contact.getCompanyId();
        description = contact.getDescription();
    }
}
