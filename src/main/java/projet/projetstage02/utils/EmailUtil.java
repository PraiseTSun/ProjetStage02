package projet.projetstage02.utils;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;

import java.util.List;
import java.util.Properties;

public class EmailUtil {

    public static JavaMailSender getJavaMailSender() {
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


    public static boolean sendConfirmationMail(AbstractUser user) {
        String userMail = user.getEmail();
        String userId = String.valueOf(user.getId());
        String userType = user instanceof Student ? "student" : user instanceof Company ? "company" : "gestionaire";
        String port = "3000";
        String URL = "http://localhost:" + port + "/confirmEmail/" +
                userId + "?userType=" + userType;
        String mailSubject = "Confirmation de l'inscription";
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("osekillerservice@outlook.com");
        mail.setSubject(mailSubject);
        mail.setTo(userMail);
        mail.setText("""
                Cliquez sur le lien suivant pour confirmer votre adresse:

                """ + URL + """

                Vous avez 24 pour confirmer votre adresse
                """);
        try {
            getJavaMailSender().send(mail);
        } catch (MailException e) {
            return false;
        }
        return true;
    }

    public static void sendNotificationMail(List<String> email, Offre offre) {
        String mailSubject = "Nouvelle offre de stage";
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("osekillerservice@outlook.com");
        mail.setSubject(mailSubject);
        mail.setTo(email.toArray(new String[0]));
        mail.setText("""
                Nouvelle offre de stage sur OseKiller:

                """ + "Poste: " + offre.getPosition() + """
                                
                """ + "Compagnie: " + offre.getNomDeCompagnie() + """
                                
                """ + "Salaire: " + offre.getSalaire() + "$/h" + """
                                
                """ + "Addresse: " + offre.getAdresse() + """
                                
                """ + "Date de début: " + offre.getDateStageDebut() + """
                                
                """ + "Date de fin: " + offre.getDateStageFin());
        try {
            getJavaMailSender().send(mail);
        } catch (MailException ignored) {
        }
    }
}
