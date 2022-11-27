package projet.projetstage02.utils;

import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Student;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailUtil {
    private static final Properties props = getProps();

    private static Properties getProps() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.user", USERNAME);
        properties.put("mail.smtp.password", PASSWORD);
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.debug", "true");
        return properties;
    }

    private static final Session session = Session.getInstance(props, null);
    private static final String HOST = "smtp-mail.outlook.com";
    private static final String USERNAME = "osekillerservice@outlook.com";
    private static final String PASSWORD = "osekiller123";
    private static final int PORT = 587;

    public static MimeMessage getMailMessage() {
        return new MimeMessage(session);
    }


    public static boolean sendConfirmationMail(AbstractUser user) {
        String userMail = user.getEmail();
        String userId = String.valueOf(user.getId());
        String userType = user instanceof Student ? "student" : user instanceof Company ? "company" : "gestionaire";
        String port = "3000";
        String url = "http://localhost:" + port + "/confirmEmail/" +
                userId + "?userType=" + userType;
        String mailSubject = "Confirmation de l'inscription";
        MimeMessage mail = getMailMessage();
        try {
            mail.setFrom(new InternetAddress(USERNAME));
            mail.setSubject(mailSubject);
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(userMail));
            mail.setContent("""
                    Cliquez sur le lien suivant pour confirmer votre adresse:
                        
                    """ + url + """
                        
                    Vous avez 24 pour confirmer votre adresse
                    """, "text/html");
            return sendMail(mail);
        } catch (Exception e) {
            return false;
        }
    }

    public static void sendNotificationMail(List<String> emails, Offre offre) {
        try {
            List<InternetAddress> bccList = new ArrayList<>();
            emails.forEach(email -> {
                try {
                    bccList.add(new InternetAddress(email));
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            });
            String mailSubject = "Nouvelle offre de stage";
            MimeMessage mail = getMailMessage();
            String text = ("""
                    Nouvelle offre de stage sur OseKiller:
                    <br />
                    <br />
                    """ + "Poste: " + offre.getPosition() + """
                    <br />
                    """ + "Compagnie: " + offre.getNomDeCompagnie() + """
                    <br />
                    """ + "Salaire: " + offre.getSalaire() + "$/h" + """
                    <br />
                    """ + "Addresse: " + offre.getAdresse() + """
                    <br />
                    """ + "Date de début: " + offre.getDateStageDebut() + """
                    <br />
                    """ + "Date de fin: " + offre.getDateStageFin());
            mail.setFrom(new InternetAddress(USERNAME));
            mail.setSubject(mailSubject);
            mail.addRecipients(Message.RecipientType.BCC, bccList.toArray(new InternetAddress[0]));
            mail.setContent(text, "text/html");
            sendMail(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean sendMail(MimeMessage mail) {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, PORT, USERNAME, PASSWORD);
            transport.sendMessage(mail, mail.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
