package projet.projetstage02.dto.contracts;

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
    private long contractId;
    private long studentId;
    private long offerId;
    private long companyId;
    private String employFullName;
    private String companyName;
    private String studentFullName;
    private String position;
    private String session;
    private String description;
    private String companySignature;
    private String companySignatureDate;
    private String gestionnaireSignature;
    private String gestionnaireSignatureDate;
    private String studentSignature;
    private String studentSignatureDate;

    public StageContractOutDTO(StageContract stageContract) {
        this.contractId = stageContract.getId();
        this.studentId = stageContract.getStudentId();
        this.offerId = stageContract.getOfferId();
        this.companyId = stageContract.getCompanyId();
        this.session = stageContract.getSession();
        this.description = stageContract.getDescription();
        this.companySignature = stageContract.getCompanySignature();
        this.gestionnaireSignature = stageContract.getGestionnaireSignature();
        this.studentSignature = stageContract.getStudentSignature();
        this.companySignatureDate =
                stageContract.getCompanySignatureDate() == null
                        ? ""
                        : stageContract.getCompanySignatureDate().toString();
        this.gestionnaireSignatureDate =
                stageContract.getGestionnaireSignatureDate() == null
                        ? ""
                        : stageContract.getGestionnaireSignatureDate().toString();
        this.studentSignatureDate =
                stageContract.getStudentSignatureDate() == null
                        ? ""
                        : stageContract.getStudentSignatureDate().toString();

    }

}
