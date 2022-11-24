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
    private int nbValidationUser;
    private int nbValidationCV;
    private int nbValidationOffer;
    private int nbEvaluateMilieuStage;
    private int nbCreateContract;
    private int nbConsultStageEvaluation;
    private int nbConsultStudentEvaluation;
    private int nbSigneContract;
}
