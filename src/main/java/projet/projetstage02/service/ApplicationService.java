package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ApplicationService {
    //TODO when merging with token branch, user TimeUtil stuff
    private final long MILLI_SECOND_DAY = 864000000;

    private GestionnaireRepository gestionnaireRepository;
    private StudentRepository studentRepository;
    private CompanyRepository companyRepository;

    public void deleteUnconfirmedUsers(){
        deleteUnconfirmedGestionnaires();
        deleteUnconfirmedStudents();
        deleteUnconfirmedCompanies();
    }

    private void deleteUnconfirmedCompanies() {
        companyRepository.findAll().stream().filter(
                company -> !company.isEmailConfirmed()
                        && Timestamp.valueOf(LocalDateTime.now()).getTime() - company.getInscriptionTimestamp() > MILLI_SECOND_DAY)
                .forEach(
                        company -> companyRepository.delete(company)
                );
    }

    private void deleteUnconfirmedStudents() {
        studentRepository.findAll().stream().filter(
                        student -> !student.isEmailConfirmed()
                                && Timestamp.valueOf(LocalDateTime.now()).getTime() - student.getInscriptionTimestamp() > MILLI_SECOND_DAY)
                .forEach(
                        student -> studentRepository.delete(student)
                );
    }

    private void deleteUnconfirmedGestionnaires() {
        gestionnaireRepository.findAll().stream().filter(
                        gestionnaire -> !gestionnaire.isEmailConfirmed()
                                && Timestamp.valueOf(LocalDateTime.now()).getTime() - gestionnaire.getInscriptionTimestamp() > MILLI_SECOND_DAY)
                .forEach(
                        gestionnaire -> gestionnaireRepository.delete(gestionnaire)
                );

    }
}
