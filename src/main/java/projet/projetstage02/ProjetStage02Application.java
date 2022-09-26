package projet.projetstage02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

@SpringBootApplication
public class ProjetStage02Application implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GestionnaireService gestionnaireService;

    public static void main(String[] args) {
        SpringApplication.run(ProjetStage02Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        studentService.saveStudent("Samir", "Badi", "Samir@gmail.com", "cooldude",
                AbstractUser.Department.Informatique);
        StudentDTO student = studentService.getStudentById(1L);
        student.setEmailConfirmed(true);
        studentService.saveStudent(student);

        companyService.saveCompany("Bob", "Marley", "Bell", "Bob@bell.com", "bestcompany",
                AbstractUser.Department.Informatique);
        CompanyDTO company = companyService.getCompanyById(2L);
        company.setEmailConfirmed(true);
        companyService.saveCompany(company);

        gestionnaireService.saveGestionnaire("Dave", "Douch", "dave@gmail.ca", "cooldude");
        GestionnaireDTO gestionnaire = gestionnaireService.getGestionnaireById(3L);
        gestionnaire.setEmailConfirmed(true);
        gestionnaireService.saveGestionnaire(gestionnaire);

        companyService.createOffre(new OffreDTO("Bell", "Informatique", "Support TI", 35, "My Home", null, false));

        System.out.println(studentService.getStudentById(1L));
        System.out.println(companyService.getCompanyById(2L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(studentService.getStudentByEmailPassword("Samir@gmail.com", "cooldude"));
        System.out.println(companyService.getCompanyByEmailPassword("Bob@bell.com", "bestcompany"));
        System.out.println(gestionnaireService.getGestionnaireByEmailPassword("dave@gmail.ca", "cooldude"));
        System.out.println(gestionnaireService.getNoneValidateOffers());
    }
}
