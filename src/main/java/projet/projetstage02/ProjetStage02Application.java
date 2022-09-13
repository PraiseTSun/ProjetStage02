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
        studentService.createStudent("Samir", "Badi", "Samir@gmail.ca", "cooldude", AbstractUser.Department.Informatique);
        companyService.createCompany("Bob", "Marley", "Bell", "Bob@bell.com", "bestcompany", AbstractUser.Department.Informatique);
        gestionnaireService.createGestionnaire("Dave", "Douch", "Dave@gmail.com", "Dave69");

        System.out.println(studentService.getUserById(1L));
        System.out.println(companyService.getUserById(2L));
        System.out.println(gestionnaireService.getUserById(3L));
        gestionnaireService.confirmUser(gestionnaireService.getUserById(3L));
        System.out.println(gestionnaireService.getUserById(3L));
        System.out.println(gestionnaireService.getUserByEmailPassword("Dave@gmail.com", "Dave69"));
        System.out.println(companyService.getUserByEmailPassword("Bob@bell.com", "bestcompany"));
        System.out.println(studentService.getUserByEmailPassword("Samir@gmail.ca", "cooldude"));
    }
}
