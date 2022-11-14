package projet.projetstage02.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationEtudiant {
    @Min(1)
    @Id
    @GeneratedValue
    private long id;
    @Min(1)
    private long contractId;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String travailEfficace;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String comprendRapidement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String rythmeSoutenu;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String etablirPriorites;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String respecteEcheances;
    private String commentairesProductivite;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String respecteMandatsDemandes;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String attentionAuxDetails;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String doubleCheckTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String occasionsDePerfectionnement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String bonneAnalyseProblemes;
    private String commentairesQualite;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String planifieTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String contactsFaciles;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String travailEnEquipe;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String adapteCulture;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String accepteCritiques;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String respecteAutres;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String ecouteActiveComprendrePDVautre;
    private String commentairesRelationsInterpersonnelles;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String interetMotivation;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String exprimeIdees;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String initiative;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String travailSecuritaire;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String responsableAutonome;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|iimpossibleDeSePrononcer")
    private String ponctuel;
    private String commentairesHabilites;
    @Pattern(regexp = "depassentBeacoupAttentes|depassentAttentes|repondentAttentes|repondentPartiellementAttentes|repondentPasAttentes")
    @NotBlank
    private String habiletesDemontres;
    private String commentairesAppreciation;
    @Pattern(regexp = "oui|non")
    @NotBlank
    private String discuteAvecStagiaire;
    @Min(0)
    private long heuresEncadrement;
    @Pattern(regexp = "oui|non|peutEtre")
    @NotBlank
    private String acueillirPourProchainStage;
    @NotBlank
    private String formationTechniqueSuffisante;
    @NotBlank
    @Lob
    private String signature;
    private LocalDate dateSignature;

    public EvaluationEtudiant(EvaluationEtudiantInDTO evaluationEtudiantInDTO) {
        this.contractId = evaluationEtudiantInDTO.getContractId();
        this.contactsFaciles = evaluationEtudiantInDTO.getContactsFaciles();
        this.acueillirPourProchainStage = evaluationEtudiantInDTO.getAcueillirPourProchainStage();
        this.adapteCulture = evaluationEtudiantInDTO.getAdapteCulture();
        this.attentionAuxDetails = evaluationEtudiantInDTO.getAttentionAuxDetails();
        this.accepteCritiques = evaluationEtudiantInDTO.getAccepteCritiques();
        this.commentairesQualite = evaluationEtudiantInDTO.getCommentairesQualite();
        this.commentairesHabilites = evaluationEtudiantInDTO.getCommentairesHabilites();
        this.commentairesRelationsInterpersonnelles = evaluationEtudiantInDTO.getCommentairesRelationsInterpersonnelles();
        this.commentairesAppreciation = evaluationEtudiantInDTO.getCommentairesAppreciation();
        this.discuteAvecStagiaire = evaluationEtudiantInDTO.getDiscuteAvecStagiaire();
        this.doubleCheckTravail = evaluationEtudiantInDTO.getDoubleCheckTravail();
        this.ecouteActiveComprendrePDVautre = evaluationEtudiantInDTO.getEcouteActiveComprendrePDVautre();
        this.formationTechniqueSuffisante = evaluationEtudiantInDTO.getFormationTechniqueSuffisante();
        this.heuresEncadrement = evaluationEtudiantInDTO.getHeuresEncadrement();
        this.habiletesDemontres = evaluationEtudiantInDTO.getHabiletesDemontres();
        this.initiative = evaluationEtudiantInDTO.getInitiative();
        this.interetMotivation = evaluationEtudiantInDTO.getInteretMotivation();
        this.occasionsDePerfectionnement = evaluationEtudiantInDTO.getOccasionsDePerfectionnement();
        this.ponctuel = evaluationEtudiantInDTO.getPonctuel();
        this.respecteAutres = evaluationEtudiantInDTO.getRespecteAutres();
        this.responsableAutonome = evaluationEtudiantInDTO.getResponsableAutonome();
        this.signature = evaluationEtudiantInDTO.getSignature();
        this.travailEnEquipe = evaluationEtudiantInDTO.getTravailEnEquipe();
        this.travailSecuritaire = evaluationEtudiantInDTO.getTravailSecuritaire();
        this.bonneAnalyseProblemes = evaluationEtudiantInDTO.getBonneAnalyseProblemes();
        this.planifieTravail = evaluationEtudiantInDTO.getPlanifieTravail();
        this.dateSignature = LocalDate.parse(evaluationEtudiantInDTO.getDateSignature());
        this.exprimeIdees = evaluationEtudiantInDTO.getExprimeIdees();
        this.travailEfficace = evaluationEtudiantInDTO.getTravailEfficace();
        this.rythmeSoutenu = evaluationEtudiantInDTO.getRythmeSoutenu();
        this.etablirPriorites = evaluationEtudiantInDTO.getEtablirPriorites();
        this.comprendRapidement = evaluationEtudiantInDTO.getComprendRapidement();
        this.commentairesProductivite = evaluationEtudiantInDTO.getCommentairesProductivite();
        this.respecteEcheances = evaluationEtudiantInDTO.getRespecteEcheances();
        this.respecteMandatsDemandes = evaluationEtudiantInDTO.getRespecteMandatsDemandes();
    }
}
