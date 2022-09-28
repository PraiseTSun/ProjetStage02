package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
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
    StudentService studentService;
    CompanyService companyService;
    GestionnaireService gestionnaireService;
    //TODO: Add new thread to remove the user after 24h hours if not email confirmed
    private final long MILLI_SECOND_DAY = 864000000;

    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        if (!studentService.isEmailUnique(studentDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        studentDTO.setInscriptionTimestamp(currentTimestamp());
        long id = studentService.saveStudent(studentDTO);
        studentDTO.setId(id);
        EmailUtil.sendConfirmationMail(studentDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        if (!companyService.isEmailUnique(companyDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        companyDTO.setInscriptionTimestamp(currentTimestamp());
        long id = companyService.saveCompany(companyDTO);
        companyDTO.setId(id);
        EmailUtil.sendConfirmationMail(companyDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        if (!gestionnaireService.isEmailUnique(gestionnaireDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
        gestionnaireDTO.setConfirmed(true);
        long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
        gestionnaireDTO.setId(id);
        EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@Valid @RequestBody OffreDTO offreDTO){
        companyService.createOffre(offreDTO);
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
        try {
            StudentDTO studentDTO = studentService.getStudentById(Long.parseLong(id));
            if (currentTimestamp() - studentDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            studentDTO.setEmailConfirmed(true);
            studentService.saveStudent(studentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/confirmEmail/company/{id}")
    public ResponseEntity<Map<String, String>> confirmCompanyMail(@PathVariable String id) {
        try {
            CompanyDTO companyDTO = companyService.getCompanyById(Long.parseLong(id));
            if (currentTimestamp() - companyDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO dto = studentService.getStudentByEmailPassword(studentDTO.getEmail(), studentDTO.getPassword());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        try {
            CompanyDTO dto = companyService.getCompanyByEmailPassword(companyDTO.getEmail(), companyDTO.getPassword());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        try {
            GestionnaireDTO dto = gestionnaireService.getGestionnaireByEmailPassword(gestionnaireDTO.getEmail(),
                    gestionnaireDTO.getPassword());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents() {
        List<StudentDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();
        unvalidatedStudents.forEach(student -> student.setPassword(""));
        return ResponseEntity.ok(unvalidatedStudents);
    }

    @GetMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies() {
        List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
        unvalidatedCompanies.forEach(company -> company.setPassword(""));
        return ResponseEntity.ok(unvalidatedCompanies);
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id) {
        try {
            gestionnaireService.validateStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getError(exception.getMessage()));
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id) {
        try {
            gestionnaireService.validateCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id) {
        try {
            gestionnaireService.removeStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id) {
        try {
            gestionnaireService.removeCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentUserException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unvalidatedOffers")
    public ResponseEntity<List<OffreDTO>> getOfferToValidate(){
        List<OffreDTO> unvalidatedOffers = gestionnaireService.getNoneValidateOffers();
        return ResponseEntity.ok(unvalidatedOffers);
    }

    @PutMapping("/validateOffer/{id}")
    public ResponseEntity<Map<String, String>> validateOffer(@PathVariable String id) {
        try {
            gestionnaireService.validateOfferById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeOffer/{id}")
    public ResponseEntity<Map<String, String>> removeOffer(@PathVariable String id) {
        try {
            gestionnaireService.removeOfferById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/offerPdf/{id}")
    public ResponseEntity<byte[]> getOfferPdf(@PathVariable String id){
        try {
            byte[] offerPdf = gestionnaireService.getOffrePdfById(Long.parseLong(id));
            return ResponseEntity.ok(offerPdf);
        } catch (NonExistentOfferExeption e) {
            return ResponseEntity.notFound().build();
        }
    }
}
