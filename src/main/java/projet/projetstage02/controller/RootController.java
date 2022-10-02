package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;
import projet.projetstage02.utils.EmailUtil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class RootController {
    final StudentService studentService;
    final CompanyService companyService;
    final GestionnaireService gestionnaireService;
    private final long MILLI_SECOND_DAY = 864000000;

    private final Logger logger = LogManager.getLogger(RootController.class);

    @SneakyThrows
    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        logger.log(Level.INFO, "PostMapping: /createStudent entered with body : " + studentDTO.toString());
        if (!studentService.isEmailUnique(studentDTO.getEmail())
                && !studentService.deleteUnconfirmedStudent(studentDTO)) {
            logger.log(Level.INFO, "PostMapping: /createStudent sent 409 response");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(getError("Cette adresse email est déjà utilisée."));
        }
        studentDTO.setInscriptionTimestamp(currentTimestamp());
        long id = studentService.saveStudent(studentDTO);
        studentDTO.setId(id);
        EmailUtil.sendConfirmationMail(studentDTO.toModel());
        logger.log(Level.INFO, "PostMapping: /createStudent sent 201 response");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @SneakyThrows
    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        logger.log(Level.INFO, "Post /createCompany entered with body : " + companyDTO.toString());
        if (!companyService.isEmailUnique(companyDTO.getEmail())
                && !companyService.deleteUnconfirmedCompany(companyDTO)) {
            logger.log(Level.INFO, "PostMapping: /createCompany sent 409 response");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(getError("Cette adresse email est déjà utilisée."));
        }
        companyDTO.setInscriptionTimestamp(currentTimestamp());
        long id = companyService.saveCompany(companyDTO);
        companyDTO.setId(id);
        EmailUtil.sendConfirmationMail(companyDTO.toModel());
        logger.log(Level.INFO, "PostMapping: /createCompany sent 201 response");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @SneakyThrows
    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        logger.log(Level.INFO, "Post /createGestionnaire entered with body : " + gestionnaireDTO.toString());
        if (!gestionnaireService.isEmailUnique(gestionnaireDTO.getEmail())
                && !gestionnaireService.deleteUnconfirmedGestionnaire(gestionnaireDTO)) {
            logger.log(Level.INFO, "PostMapping: /createGestionnaire sent 409 response");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(getError("Cette adresse email est déjà utilisée."));
        }
        gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
        gestionnaireDTO.setConfirmed(true);
        long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
        gestionnaireDTO.setId(id);
        EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel());
        logger.log(Level.INFO, "PostMapping: /createGestionnaire sent 201 response");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@Valid @RequestBody OffreDTO offreDTO) {
        logger.log(Level.INFO, "Post /createOffre entered with body : " + offreDTO.toString());
        companyService.createOffre(offreDTO);
        logger.log(Level.INFO, "PostMapping: /createOffre sent 201 response");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private long currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    private Map<String, String> getError(String error) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("error", error);
        return hashMap;
    }

    @PutMapping("/confirmEmail/student/{id}")
    public ResponseEntity<Map<String, String>> confirmStudentMail(@PathVariable String id) {
        logger.log(Level.INFO, "Put /confirmEmail/student/{id} entered with id : " + id);
        try {
            StudentDTO studentDTO = studentService.getStudentById(Long.parseLong(id));
            if (currentTimestamp() - studentDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(Level.INFO, "PutMapping: /confirmEmail/student sent 400 response");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            studentDTO.setEmailConfirmed(true);
            studentService.saveStudent(studentDTO);
            logger.log(Level.INFO, "PutMapping: /confirmEmail/student sent 201 response");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentUserException e) {
            logger.log(Level.INFO, "PutMapping: /confirmEmail/student sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/confirmEmail/company/{id}")
    public ResponseEntity<Map<String, String>> confirmCompanyMail(@PathVariable String id) {
        logger.log(Level.INFO, "Put /confirmEmail/company/{id} entered with id : " + id);
        try {
            CompanyDTO companyDTO = companyService.getCompanyById(Long.parseLong(id));
            if (currentTimestamp() - companyDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(Level.INFO, "PutMapping: /confirmEmail/company sent 400 response");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            logger.log(Level.INFO, "PutMapping: /confirmEmail/company sent 201 response");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentUserException e) {
            logger.log(Level.INFO, "PutMapping: /confirmEmail/company sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@Valid @RequestBody LoginDTO loginDTO) {
        logger.log(Level.INFO, "Put /student entered with body : " + loginDTO.toString());
        try {
            StudentDTO dto = studentService.getStudentByEmailPassword(loginDTO.getEmail(), loginDTO.getPassword());
            dto.setPassword("");
            logger.log(Level.INFO, "PutMapping: /student sent "
                    + (!dto.isEmailConfirmed() ? "404" : "200") + " response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            logger.log(Level.INFO, "PutMapping: /student sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@Valid @RequestBody LoginDTO loginDTO) {
        logger.log(Level.INFO, "Put /company entered with body : " + loginDTO.toString());
        try {
            CompanyDTO dto = companyService.getCompanyByEmailPassword(loginDTO.getEmail(), loginDTO.getPassword());
            dto.setPassword("");
            logger.log(Level.INFO, !dto.isEmailConfirmed() ?
                    "Put /company entered sent 400 response" :
                    "Put /company entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            logger.log(Level.INFO, "Put /company entered sent 400 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@Valid @RequestBody LoginDTO loginDTO) {
        logger.log(Level.INFO, "Put /gestionnaire entered with body : " + loginDTO.toString());
        try {
            GestionnaireDTO dto = gestionnaireService.getGestionnaireByEmailPassword(loginDTO.getEmail(),
                    loginDTO.getPassword());
            dto.setPassword("");
            logger.log(Level.INFO, !dto.isEmailConfirmed() ?
                    "Put /gestionnaire entered sent 400 response" :
                    "Put /gestionnaire entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            logger.log(Level.INFO, "PutMapping: /gestionnaire sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents() {
        logger.log(Level.INFO, "get /unvalidatedStudents entered");
        List<StudentDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();
        unvalidatedStudents.forEach(student -> student.setPassword(""));
        logger.log(Level.INFO, "GetMapping: /unvalidatedStudents sent 200 response");
        return ResponseEntity.ok(unvalidatedStudents);
    }

    @GetMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies() {
        logger.log(Level.INFO, "get /unvalidatedCompanies entered");
        List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
        unvalidatedCompanies.forEach(company -> company.setPassword(""));
        logger.log(Level.INFO, "GetMapping: /unvalidatedCompanies sent 200 response");
        return ResponseEntity.ok(unvalidatedCompanies);
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id) {
        logger.log(Level.INFO, "Put /validateStudent/{id} entered with id : " + id);
        try {
            gestionnaireService.validateStudent(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            logger.log(Level.INFO, "PutMapping: /validateStudent sent 404 response");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getError(exception.getMessage()));
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id) {
        logger.log(Level.INFO, "Put /validateCompany/{id} entered with id : " + id);
        try {
            gestionnaireService.validateCompany(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            logger.log(Level.INFO, "PutMapping: /validateCompany sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id) {
        logger.log(Level.INFO, "Delete /removeStudent/{id} entered with id : " + id);
        try {
            gestionnaireService.removeStudent(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeStudent sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id) {
        logger.log(Level.INFO, "Delete /removeCompany/{id} entered with id : " + id);
        try {
            gestionnaireService.removeCompany(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeCompany sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unvalidatedOffers")
    public ResponseEntity<List<OffreDTO>> getOfferToValidate() {
        logger.log(Level.INFO, "get /unvalidatedOffers entered");
        List<OffreDTO> unvalidatedOffers = gestionnaireService.getNoneValidateOffers();
        logger.log(Level.INFO, "GetMapping: /unvalidatedOffers sent 200 response");
        return ResponseEntity.ok(unvalidatedOffers);
    }

    @PutMapping("/validateOffer/{id}")
    public ResponseEntity<Map<String, String>> validateOffer(@PathVariable String id) {
        logger.log(Level.INFO, "Put /validateOffer/{id} entered with id : " + id);
        try {
            gestionnaireService.validateOfferById(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateOffer sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            logger.log(Level.INFO, "PutMapping: /validateOffer sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeOffer/{id}")
    public ResponseEntity<Map<String, String>> removeOffer(@PathVariable String id) {
        logger.log(Level.INFO, "Delete /removeOffer/{id} entered with id : " + id);
        try {
            gestionnaireService.removeOfferById(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeOffer sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeOffer sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/offerPdf/{id}")
    public ResponseEntity<byte[]> getOfferPdf(@PathVariable String id) {
        logger.log(Level.INFO, "Get /offerPdf/{id} entered with id : " + id);
        try {
            byte[] offerPdf = gestionnaireService.getOffrePdfById(Long.parseLong(id));
            logger.log(Level.INFO, "Get /offerPdf sent 200 response");
            return ResponseEntity.ok(offerPdf);
        } catch (NonExistentOfferExeption e) {
            logger.log(Level.INFO, "Get /offerPdf sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }
}
