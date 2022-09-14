package projet.projetstage02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import projet.projetstage02.modele.AbstractUser;
import projet.projetstage02.modele.Company;
import projet.projetstage02.modele.Student;

public abstract class AbstractService<T> {
    public abstract boolean isUniqueEmail(String email);
    public abstract T getUserById(Long id);
    public abstract T getUserByEmailPassword(String email, String password);
    @Lazy
    @Autowired
    private JavaMailSender mailSender;
    public boolean sendConfirmationMail(AbstractUser user){
        String userMail = user.getEmail();
        String userId = String.valueOf(user.getId());
        String userType = user instanceof Student ? "student":
                user instanceof Company ?"company":"gestionaire";
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
        try{
            mailSender.send(mail);
        }catch (MailException e){
            return  false;
        }
        return true;
    }
}
