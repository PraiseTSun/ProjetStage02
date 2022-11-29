package projet.projetstage02;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootApplication
@AllArgsConstructor
public class ProjetStage02Application implements CommandLineRunner {
    private StudentService studentService;
    private CompanyService companyService;
    private GestionnaireService gestionnaireService;

    public static void main(String[] args) {
        SpringApplication.run(ProjetStage02Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        byte[] inFileBytes = Files.readAllBytes(Paths.get("OffreVideoTron.pdf"));

        long samirId = studentService.saveStudent(StudentInDTO.builder()
                .firstName("Samir")
                .lastName("Badi")
                .email("samir@gmail.com".toLowerCase())
                .password("cooldude")
                .department(AbstractUser.Department.Informatique.departement)
                .isConfirmed(true)
                .emailConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build());


        long joeBidenId = studentService.saveStudent(StudentInDTO.builder()
                .firstName("Joe")
                .lastName("Biden")
                .email("joe.biden@gov.com".toLowerCase())
                .password("imnotsenile")
                .department(AbstractUser.Department.Informatique.departement)
                .isConfirmed(false)
                .emailConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build());

        long francoisId = gestionnaireService.saveGestionnaire(GestionnaireDTO.builder()
                .firstName("Francois")
                .lastName("Lacoursiere")
                .email("admin@osekiller.com".toLowerCase())
                .password("ilovevscode")
                .emailConfirmed(true)
                .isConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build());

        long videotronId = companyService.saveCompany(CompanyDTO.builder()
                .firstName("Michael")
                .lastName("Jackson")
                .email("michael@videotron.com".toLowerCase())
                .password("bestcompany")
                .isConfirmed(true)
                .emailConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .department(Department.Informatique.departement)
                .companyName("Videotron")
                .build());

        long bellId = companyService.saveCompany(CompanyDTO.builder()
                .firstName("Bob")
                .lastName("Marley")
                .email("Bob@bell.com".toLowerCase())
                .password("bestcompany")
                .isConfirmed(false)
                .emailConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .department(Department.Transport.departement)
                .companyName("Bell")
                .build());


        companyService.saveCompany(CompanyDTO.builder()
                .firstName("Massi")
                .lastName("Djellouli")
                .email("m.dj@sobeys.com".toLowerCase())
                .password("bestcompany")
                .isConfirmed(false)
                .emailConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .department(Department.Transport.departement)
                .companyName("IGA")
                .build());

        long offreId = companyService.createOffre(OffreInDTO.builder()
                .adresse("400 QC-132 Local 125")
                .department(Department.Informatique.departement)
                .heureParSemaine(40)
                .companyId(videotronId)
                .dateStageDebut("2023-01-25")
                .dateStageFin("2023-05-15")
                .salaire(40)
                .session("Hiver 2023")
                .nomDeCompagnie("Videotron")
                .position("Fullstack developer")
                .pdf(inFileBytes)
                .build());

        gestionnaireService.validateOfferById(offreId);

    }
}