package projet.projetstage02.DTO;


import lombok.Data;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;

@Data
public class EvaluationInfoDTO {
    private String session;
    private String dateStage;
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

    public EvaluationInfoDTO(Company company, Offre offre, Student student) {
        session = offre.getSession();
        dateStage = offre.getDateStage();
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


