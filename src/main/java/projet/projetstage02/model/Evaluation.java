package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.DTO.EvaluationInDTO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evaluation {
    @Id
    @Min(1)
    private long id;
    @Min(1)
    private long contractId;
    @NotBlank
    private String tachesAnnonces;
    @NotBlank
    private String integration;
    @NotBlank
    private String tempsReelConsacre;
    @NotBlank
    private String environementTravail;
    @NotBlank
    private String climatTravail;
    @NotBlank
    private String milieuDeStage;
    @Min(1)
    private long heureTotalPremierMois;
    @Min(1)
    private long heureTotalDeuxiemeMois;
    @Min(1)
    private long heureTotalTroisiemeMois;
    @NotBlank
    private String communicationAvecSuperviser;
    @NotBlank
    private String equipementFourni;
    @NotBlank
    private String volumeDeTravail;
    @NotBlank
    private String commentaires;
    @NotBlank
    private String signature;
    @NotBlank
    private String dateSignature;

    public Evaluation(EvaluationInDTO evaluationInDTO) {
        contractId = evaluationInDTO.getContractId();
        tachesAnnonces = evaluationInDTO.getTachesAnnonces();
        integration = evaluationInDTO.getIntegration();
        tempsReelConsacre = evaluationInDTO.getTempsReelConsacre();
        environementTravail = evaluationInDTO.getEnvironementTravail();
        climatTravail = evaluationInDTO.getClimatTravail();
        milieuDeStage = evaluationInDTO.getMilieuDeStage();
        heureTotalPremierMois = evaluationInDTO.getHeureTotalPremierMois();
        heureTotalDeuxiemeMois = evaluationInDTO.getHeureTotalDeuxiemeMois();
        heureTotalTroisiemeMois = evaluationInDTO.getHeureTotalTroisiemeMois();
        communicationAvecSuperviser = evaluationInDTO.getCommunicationAvecSuperviser();
        equipementFourni = evaluationInDTO.getEquipementFourni();
        volumeDeTravail = evaluationInDTO.getVolumeDeTravail();
        commentaires = evaluationInDTO.getCommentaires();
        signature = evaluationInDTO.getSignature();
        dateSignature = evaluationInDTO.getDateSignature();
    }
}
