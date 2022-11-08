package projet.projetstage02.dto.evaluations.MillieuStage;


import lombok.Data;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;

@Data
public class MillieuStageEvaluationInfoDTO {
    private String session;
    private String dateStageDebut;
    private String dateStageFin;
    private String poste;
    private long salaire;
    private long heureParSemaine;
    private String departement;
    private String nomCompagnie;
    private String nomContact;
    private String prenomContact;
    private String emailCompagnie;
    private String adresse;
    private String nomEtudiant;
    private String prenomEtudiant;
    private String emailEtudiant;

    public MillieuStageEvaluationInfoDTO(Company company, Offre offre, Student student) {
        session = offre.getSession();
        dateStageDebut = offre.getDateStageDebut();
        dateStageFin = offre.getDateStageFin();
        poste = offre.getPosition();
        salaire = offre.getSalaire();
        heureParSemaine = offre.getHeureParSemaine();
        departement = offre.getDepartment().departement;
        nomCompagnie = company.getCompanyName();
        nomContact = company.getLastName();
        prenomContact = company.getFirstName();
        emailCompagnie = company.getEmail();
        adresse = offre.getAdresse();
        nomEtudiant = student.getLastName();
        prenomEtudiant = student.getFirstName();
        emailEtudiant = student.getEmail();
    }

}


