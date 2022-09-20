package projet.projetstage02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.OffreService;
import projet.projetstage02.service.StudentService;

import java.io.*;
import java.util.Properties;

@SpringBootApplication
public class ProjetStage02Application implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GestionnaireService gestionnaireService;

    @Autowired
    private OffreService offreService;
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
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.debug", "true");
        return mailSender;
    }

    @Override
    public void run(String... args) throws Exception {
        studentService.saveStudent("Samir", "Badi", "Samir@gmail.com", "cooldude",
                AbstractUser.Department.Informatique);
        StudentDTO student = studentService.getUserById(1L);
        student.setEmailConfirmed(true);
        studentService.saveStudent(student);

        companyService.saveCompany("Bob", "Marley", "Bell", "Bob@bell.com", "bestcompany",
                AbstractUser.Department.Informatique);
        CompanyDTO company = companyService.getUserById(2L);
        company.setEmailConfirmed(true);
        companyService.saveCompany(company);

        gestionnaireService.saveGestionnaire("Dave", "Douch", "dave@gmail.ca", "cooldude");
        GestionnaireDTO gestionnaire = gestionnaireService.getUserById(3L);
        gestionnaire.setEmailConfirmed(true);
        gestionnaireService.saveGestionnaire(gestionnaire);

        System.out.println(studentService.getUserById(1L));
        System.out.println(companyService.getUserById(2L));
        System.out.println(gestionnaireService.getUserById(3L));
        gestionnaireService.confirmUser(gestionnaireService.getUserById(3L));
        System.out.println(gestionnaireService.getUserById(3L));
        System.out.println(studentService.getUserByEmailPassword("Samir@gmail.com", "cooldude"));
        System.out.println(companyService.getUserByEmailPassword("Bob@bell.com", "bestcompany"));
        System.out.println(gestionnaireService.getUserByEmailPassword("dave@gmail.ca", "cooldude"));

//créer deux offre avec pdf
        // un
        //  OffreDTO offreDTO = new OffreDTO( "nom", "department", "position" ,
        //          40, "adresse", new File("test.pdf"));
        //  long offreid = offreService.createOffre(offreDTO);
        // deux
        //  OffreDTO offreDTO1 = new OffreDTO( "nom", "department", "position" ,
        //         40, "adresse", new File("test1.pdf"));
        // long offreid1 = offreService.createOffre(offreDTO1);
        // afficher
        //   OffreDTO offreDTO1 = new OffreDTO( "nom", "department", "position" ,
        //                    40, "adresse", "wowoowowowo");
        // long offreid = offreService.createOffre(offreDTO1);
        //System.out.println(offreService.findOffre());
// tester fonction isVide et valide du pdf
    }
}
