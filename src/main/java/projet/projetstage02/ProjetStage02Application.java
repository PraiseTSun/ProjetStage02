package projet.projetstage02;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.projetstage02.DTO.*;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

@SpringBootApplication
@AllArgsConstructor
public class ProjetStage02Application implements CommandLineRunner {

    private final byte[] TEST_PDF = new byte[]{
            37, 80, 68, 70, 45, 49, 46, 55, 10, 10, 49, 32, 48, 32, 111, 98, 106, 32, 32, 37, 32, 101, 110, 116,
            114, 121, 32, 112, 111, 105, 110, 116, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 67,
            97, 116, 97, 108, 111, 103, 10, 32, 32, 47, 80, 97, 103, 101, 115, 32, 50, 32, 48, 32, 82, 10, 62,
            62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 50, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32,
            47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 10, 32, 32, 47, 77, 101, 100, 105, 97, 66,
            111, 120, 32, 91, 32, 48, 32, 48, 32, 50, 48, 48, 32, 50, 48, 48, 32, 93, 10, 32, 32, 47, 67, 111,
            117, 110, 116, 32, 49, 10, 32, 32, 47, 75, 105, 100, 115, 32, 91, 32, 51, 32, 48, 32, 82, 32, 93,
            10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 51, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10,
            32, 32, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 10, 32, 32, 47, 80, 97, 114, 101, 110, 116,
            32, 50, 32, 48, 32, 82, 10, 32, 32, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 10,
            32, 32, 32, 32, 47, 70, 111, 110, 116, 32, 60, 60, 10, 32, 32, 32, 32, 32, 32, 47, 70, 49, 32, 52,
            32, 48, 32, 82, 32, 10, 32, 32, 32, 32, 62, 62, 10, 32, 32, 62, 62, 10, 32, 32, 47, 67, 111, 110,
            116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10,
            10, 52, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 70, 111,
            110, 116, 10, 32, 32, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 10, 32,
            32, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 84, 105, 109, 101, 115, 45, 82, 111, 109, 97,
            110, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 53, 32, 48, 32, 111, 98, 106, 32, 32, 37,
            32, 112, 97, 103, 101, 32, 99, 111, 110, 116, 101, 110, 116, 10, 60, 60, 10, 32, 32, 47, 76, 101,
            110, 103, 116, 104, 32, 52, 52, 10, 62, 62, 10, 115, 116, 114, 101, 97, 109, 10, 66, 84, 10, 55, 48,
            32, 53, 48, 32, 84, 68, 10, 47, 70, 49, 32, 49, 50, 32, 84, 102, 10, 40, 72, 101, 108, 108, 111, 44,
            32, 119, 111, 114, 108, 100, 33, 41, 32, 84, 106, 10, 69, 84, 10, 101, 110, 100, 115, 116, 114, 101,
            97, 109, 10, 101, 110, 100, 111, 98, 106, 10, 10, 120, 114, 101, 102, 10, 48, 32, 54, 10, 48, 48,
            48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 32, 10, 48, 48, 48, 48, 48, 48, 48,
            48, 49, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 48, 55, 57, 32, 48,
            48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 49, 55, 51, 32, 48, 48, 48, 48, 48, 32,
            110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 51, 48, 49, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48,
            48, 48, 48, 48, 48, 48, 51, 56, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 116, 114, 97, 105, 108,
            101, 114, 10, 60, 60, 10, 32, 32, 47, 83, 105, 122, 101, 32, 54, 10, 32, 32, 47, 82, 111, 111, 116,
            32, 49, 32, 48, 32, 82, 10, 62, 62, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 10, 52, 57, 50,
            10, 37, 37, 69, 79, 70
    };
    private StudentService studentService;
    private CompanyService companyService;
    private GestionnaireService gestionnaireService;

    public static void main(String[] args) {
        SpringApplication.run(ProjetStage02Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        studentService.saveStudent("Samir", "Badi", "Samir@gmail.com", "cooldude",
                AbstractUser.Department.Informatique);
        StudentOutDTO student = studentService.getStudentById(1L);
        student.setEmailConfirmed(true);
        studentService.saveStudent(new StudentInDTO(student.toModel()));

        companyService.saveCompany("Bob", "Marley", "Bell", "Bob@bell.com", "bestcompany",
                AbstractUser.Department.Informatique);
        CompanyDTO company = companyService.getCompanyById(2L);
        company.setEmailConfirmed(true);
        companyService.saveCompany(company);

        gestionnaireService.saveGestionnaire("Dave", "Douch", "dave@gmail.ca", "cooldude");
        GestionnaireDTO gestionnaire = gestionnaireService.getGestionnaireById(3L);
        gestionnaire.setEmailConfirmed(true);
        gestionnaireService.saveGestionnaire(gestionnaire);

        studentService.saveStudent("Peter", "Griffin", "peter.griffin@quahog.com", "loislois",
                AbstractUser.Department.Informatique);
        StudentOutDTO student2 = studentService.getStudentByEmailPassword("peter.griffin@quahog.com", "loislois");
        student2.setEmailConfirmed(true);
        studentService.saveStudent(new StudentInDTO(student2.toModel()));

        long offreId = companyService.createOffre(OffreInDTO.builder()
                .adresse("123 Joe Road")
                .department(Department.Informatique.departement)
                .heureParSemaine(40)
                .companyId(company.getId())
                .salaire(40)
                .session("Hiver 2023")
                .nomDeCompagnie("Bell")
                .companyId(company.getId())
                .position("Delivery Man")
                .pdf(TEST_PDF)
                .build());
        gestionnaireService.validateOfferById(offreId);

        studentService.uploadCurriculumVitae(PdfDTO.builder()
                .studentId(1L)
                .pdf(TEST_PDF)
                .build());

        studentService.uploadCurriculumVitae(PdfDTO.builder()
                .studentId(student2.getId())
                .pdf(TEST_PDF)
                .build());

        companyService.createOffre(
                OffreInDTO.builder()
                        .id(0L)
                        .nomDeCompagnie("Bell")
                        .companyId(company.getId())
                        .department("Techniques de linformatique")
                        .position("Support TI")
                        .heureParSemaine(35)
                        .salaire(35)
                        .session("Hiver 2023")
                        .adresse("My Home")
                        .pdf(TEST_PDF)
                        .companyId(company.getId())
                        .token("notoken")
                        .valide(false)
                        .build()
        );
        companyService.createOffre(
                OffreInDTO.builder()
                        .id(0L)
                        .nomDeCompagnie("Bell")
                        .companyId(company.getId())
                        .department("Techniques de linformatique")
                        .position("Support TI")
                        .heureParSemaine(36)
                        .salaire(15)
                        .session("Hiver 2023")
                        .companyId(company.getId())
                        .adresse("33 My Home")
                        .pdf(TEST_PDF)
                        .token("notoken")
                        .valide(false)
                        .build()
        );
        gestionnaireService.validateStudent(student.getId());
        gestionnaireService.validateStudent(student2.getId());
        gestionnaireService.validateCompany(company.getId());
        gestionnaireService.validateStudentCV(student.getId());
        System.out.println(studentService.getStudentById(1L));
        System.out.println(companyService.getCompanyById(2L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(studentService.getStudentByEmailPassword("Samir@gmail.com", "cooldude"));
        System.out.println(companyService.getCompanyByEmailPassword("Bob@bell.com", "bestcompany"));
        System.out.println(gestionnaireService.getGestionnaireByEmailPassword("dave@gmail.ca", "cooldude"));
        System.out.println(gestionnaireService.getUnvalidatedOffers());
        System.out.println(gestionnaireService.getUnvalidatedCVStudents());
        studentService.createPostulation(student.getId(), offreId);
        studentService.createPostulation(student2.getId(), offreId);
        companyService.saveStudentApplicationAccepted(offreId, student.getId());
        companyService.saveStudentApplicationAccepted(offreId, student2.getId());
        System.out.println(gestionnaireService.createStageContract(
                new StageContractInDTO("noToken", student.getId(), offreId)
        ));
        System.out.println(gestionnaireService.createStageContract(
                new StageContractInDTO("noToken", student2.getId(), offreId)
        ));
    }
}
