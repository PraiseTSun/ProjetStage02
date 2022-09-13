package projet.projetstage02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.projetstage02.modele.AbstractUser;
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
        studentService.createStudent("Samir", "Badi", "email", "password", AbstractUser.Department.Informatique);
        companyService.createCompany("Bob", "Marley", "Bell", "email", "password", AbstractUser.Department.Informatique);
        gestionnaireService.createGestionnaire("Dave", "Douch", "email", "password");

        System.out.println(studentService.getUserById(1L));
        System.out.println(companyService.getUserById(2L));
        System.out.println(gestionnaireService.getGestionnaire(3L));
        gestionnaireService.confirmUser(gestionnaireService.getGestionnaire(3L));
        System.out.println(gestionnaireService.getGestionnaire(3L));
    }
}
