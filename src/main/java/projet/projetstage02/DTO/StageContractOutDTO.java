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
    private long id;
    private long studentId;
    private long offerId;
    private long companyId;
    private String employFullName;
    private String companyName;
    private String studentFullName;
    private String position;
    private String description;
    private String companySignature;
    private String gestionnaireSignature;
    private String studentSignature;

    public StageContractOutDTO(StageContract stageContract) {
        this.id = stageContract.getId();
        this.studentId = stageContract.getStudentId();
        this.offerId = stageContract.getOfferId();
        this.companyId = stageContract.getCompanyId();
        this.description = stageContract.getDescription();
        this.companySignature = stageContract.getCompanySignature();
        this.gestionnaireSignature = stageContract.getGestionnaireSignature();
        this.studentSignature = stageContract.getStudentSignature();
    }

}
