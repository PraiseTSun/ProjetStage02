package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.ApplicationAcceptation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationAcceptationDTO {
    private long id;
    private long studentId;
    private long offerId;
    private String studentName;
    private String companyName;

    public ApplicationAcceptationDTO(ApplicationAcceptation application){
        id = application.getId();
        studentId = application.getStudentId();
        offerId = application.getOfferId();
        studentName = application.getStudentName();
        companyName = application.getCompanyName();
    }
}
