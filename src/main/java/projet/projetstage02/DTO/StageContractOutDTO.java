package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.StageContract;

import static projet.projetstage02.utils.ByteConverter.byteToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageContractOutDTO {
    private long contractId;
    private long studentId;
    private long offerId;
    private long companyId;
    private String session;
    private String description;
    private String companySignature;
    private String companySignatureDate;
    private String studentSignature;
    private String studentSignatureDate;
    public StageContractOutDTO (StageContract contact){
        contractId = contact.getId();
        studentId = contact.getStudentId();
        offerId = contact.getOfferId();
        companyId = contact.getCompanyId();
        session = contact.getSession();
        description = contact.getDescription();
        companySignature = contact.getCompanySignature();
        companySignatureDate = contact.getCompanySignatureDate() == null ? "" : contact.getCompanySignatureDate().toString();
        studentSignature = byteToString(contact.getStudentSignature());
        studentSignatureDate = contact.getStudentSignatureDate() == null ? "" : contact.getStudentSignatureDate().toString();
    }
}
