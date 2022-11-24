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
    private int nbUnvalidatedUser;
    private int nbUnvalidatedCV;
    private int nbUnvalidatedOffer;
    private int nbEvaluateMilieuStage;
    private int nbCreateContract;
    private int nbConsultStageEvaluation;
    private int nbConsultStudentEvaluation;
    private int nbSigneContract;
}
