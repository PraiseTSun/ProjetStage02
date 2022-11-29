package projet.projetstage02.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GestionnaireNotificationDTO {
    private long nbUnvalidatedUser;
    private long nbUnvalidatedCV;
    private long nbUnvalidatedOffer;
    private long nbEvaluateMilieuStage;
    private long nbCreateContract;
    private long nbConsultStageEvaluation;
    private long nbConsultStudentEvaluation;
    private long nbSigneContract;
}
