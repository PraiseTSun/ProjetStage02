package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.OffreService;
import projet.projetstage02.service.StudentService;

import java.io.IOException;
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

    @Autowired
    OffreService offreService;
    private final long MILLI_SECOND_DAY = 864000000;

    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@RequestBody StudentDTO studentDTO) {
        if (!studentService.isUniqueEmail(studentDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        studentDTO.setInscriptionTimestamp(currentTimestamp());
        long id = studentService.saveStudent(studentDTO);
        studentDTO.setId(String.valueOf(id));
        studentService.sendConfirmationMail(studentDTO.getClassOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@RequestBody CompanyDTO companyDTO) {
        if (!companyService.isUniqueEmail(companyDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        companyDTO.setInscriptionTimestamp(currentTimestamp());
        long id = companyService.saveCompany(companyDTO);
        companyDTO.setId(String.valueOf(id));
        companyService.sendConfirmationMail(companyDTO.getClassOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@RequestBody GestionnaireDTO gestionnaireDTO) {
        if (!gestionnaireService.isUniqueEmail(gestionnaireDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
        long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
        try {
            gestionnaireService.validateGestionnaire(id);
        } catch (NonExistentUserException e) {
            // Won't happen
            ResponseEntity.notFound().build();
        }
        gestionnaireDTO.setId(String.valueOf(id));
        gestionnaireService.sendConfirmationMail(gestionnaireDTO.getClassOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@RequestBody OffreDTO offreDTO) throws IOException {

        if(offreDTO.getPdf() == null || offreDTO.getNomDeCompagnie() == null || offreDTO.getAdresse() == null
            || offreDTO.getPosition() == null || offreDTO.getDepartment() == null
                || offreDTO.getHeureParSemaine() == 0 ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        /**
        if(!offreService.valide(offreDTO.getPdf())){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(getError("Doit être un fichier pdf"));
        }

        if(offreService.isVide(offreDTO.getPdf())){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(getError("PDF ne peut pas être vide"));
        }
         **/
        offreService.createOffre(offreDTO);
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
            StudentDTO studentDTO = studentService.getUserById(Long.parseLong(id));
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
            CompanyDTO companyDTO = companyService.getUserById(Long.parseLong(id));
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

    @PutMapping("/confirmEmail/gestionnaire/{id}")
    public ResponseEntity<Map<String, String>> confirmGestionnaireMail(@PathVariable String id) {
        try {
            GestionnaireDTO gestionnaireDTO = gestionnaireService.getUserById(Long.parseLong(id));
            if (currentTimestamp() - gestionnaireDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            gestionnaireDTO.setEmailConfirmed(true);
            gestionnaireService.saveGestionnaire(gestionnaireDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO dto = studentService.getUserByEmailPassword(studentDTO.getEmail(), studentDTO.getPassword());
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@RequestBody CompanyDTO companyDTO) {
        try {
            CompanyDTO dto = companyService.getUserByEmailPassword(companyDTO.getEmail(), companyDTO.getPassword());
            return dto == null || !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@RequestBody GestionnaireDTO gestionnaireDTO) {
        try {
            GestionnaireDTO dto = gestionnaireService.getUserByEmailPassword(gestionnaireDTO.getEmail(),
                    gestionnaireDTO.getPassword());
            return dto == null || !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents() {
        return ResponseEntity.ok(studentService.getUnvalidatedUsers());
    }

    @GetMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies() {
        return ResponseEntity.ok(companyService.getUnvalidatedUsers());
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
}
