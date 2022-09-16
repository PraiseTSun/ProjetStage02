package projet.projetstage02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

import java.util.Properties;

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
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp-mail.outlook.com");
        mailSender.setPort(587);
        mailSender.setUsername("osekillerservice@outlook.com");
        mailSender.setPassword("osekiller123");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.debug", "true");
        return mailSender;
    }
    @Override
    public void run(String... args) throws Exception {
        studentService.saveStudent("Samir", "Badi", "email", "password", AbstractUser.Department.Informatique);
        companyService.saveCompany("Bob", "Marley", "Bell", "email", "password", AbstractUser.Department.Informatique);
        gestionnaireService.saveGestionnaire("Dave", "Douch", "email", "password");

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
