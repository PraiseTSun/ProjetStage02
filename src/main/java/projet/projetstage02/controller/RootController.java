package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;
import projet.projetstage02.utils.EmailUtil;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static projet.projetstage02.model.Token.UserTypes.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class RootController {
    StudentService studentService;
    CompanyService companyService;
    GestionnaireService gestionnaireService;
    AuthService authService;
    //TODO: Add new thread to remove the user after 24h hours if not email confirmed
    private final long MILLI_SECOND_DAY = 864000000;

    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@RequestBody StudentDTO studentDTO) {
        if (!studentService.isEmailUnique(studentDTO.getEmail())) {
            return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        studentDTO.setInscriptionTimestamp(currentTimestamp());
        long id = studentService.saveStudent(studentDTO);
        studentDTO.setId(id);
        EmailUtil.sendConfirmationMail(studentDTO.toModel());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@RequestBody CompanyDTO companyDTO) {
        if (!companyService.isEmailUnique(companyDTO.getEmail())) {
            return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        companyDTO.setInscriptionTimestamp(currentTimestamp());
        long id = companyService.saveCompany(companyDTO);
        companyDTO.setId(id);
        EmailUtil.sendConfirmationMail(companyDTO.toModel());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@RequestBody GestionnaireDTO gestionnaireDTO) {
        if (!gestionnaireService.isEmailUnique(gestionnaireDTO.getEmail())) {
            return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
        }
        gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
        gestionnaireDTO.setConfirmed(true);
        long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
        gestionnaireDTO.setId(id);
        EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@RequestBody OffreDTO offreDTO){
        try{
            Token token = authService.getToken(offreDTO.getToken(), COMPANY);
            companyService.getCompanyById(token.getUserId());
            if(offreDTO.getPdf() == null || offreDTO.getNomDeCompagnie() == null || offreDTO.getAdresse() == null
                || offreDTO.getPosition() == null || offreDTO.getDepartment() == null
                    || offreDTO.getHeureParSemaine() == 0 ){
                throw new IllegalArgumentException();
            }
            companyService.createOffre(offreDTO);
            return ResponseEntity.status(CREATED).build();
        }catch (NonExistentEntityException | IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
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
                return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            studentDTO.setEmailConfirmed(true);
            studentService.saveStudent(studentDTO);
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/confirmEmail/company/{id}")
    public ResponseEntity<Map<String, String>> confirmCompanyMail(@PathVariable String id) {
        try {
            CompanyDTO companyDTO = companyService.getCompanyById(Long.parseLong(id));
            if (currentTimestamp() - companyDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/confirmEmail/gestionnaire/{id}")
    public ResponseEntity<Map<String, String>> confirmGestionnaireMail(@PathVariable String id) {
        try {
            GestionnaireDTO gestionnaireDTO = gestionnaireService.getGestionnaireById(Long.parseLong(id));
            if (currentTimestamp() - gestionnaireDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            gestionnaireDTO.setEmailConfirmed(true);
            gestionnaireService.saveGestionnaire(gestionnaireDTO);
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/student/login")
    public ResponseEntity<Map<String,String>> studentLogin(@RequestBody StudentDTO studentDTO){
        try {
            String token = authService.loginIfValid(studentDTO);
            return ResponseEntity.status(CREATED).body(formatToken(token));
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/gestionnaire/login")
    public ResponseEntity<Map<String,String>> gestionnaireLogin(@RequestBody GestionnaireDTO gestionnaireDTO){
        try {
            String token = authService.loginIfValid(gestionnaireDTO);
            return ResponseEntity.status(CREATED).body(formatToken(token));
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/company/login")
    public ResponseEntity<Map<String,String>> companyLogin(@RequestBody CompanyDTO companyDTO){
        try {
            String token = authService.loginIfValid(companyDTO);
            return ResponseEntity.status(CREATED).body(formatToken(token));
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }

    }

    private Map<String,String> formatToken(String token) {
        HashMap<String, String> toReturn = new HashMap<>();
        toReturn.put("token",token);
        return toReturn;
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), STUDENT);
            StudentDTO dto = studentService.getStudentById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), COMPANY);

            CompanyDTO dto = companyService.getCompanyById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            GestionnaireDTO dto = gestionnaireService.getGestionnaireById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents(@RequestBody Map<String,String> tokenId) {
        try{
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            List<StudentDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();
            unvalidatedStudents.forEach(student -> student.setPassword(""));
            return ResponseEntity.ok(unvalidatedStudents);
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies(@RequestBody Map<String,String> tokenId)  {
        try{
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
            unvalidatedCompanies.forEach(company -> company.setPassword(""));
            return ResponseEntity.ok(unvalidatedCompanies);
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id , @RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getError(exception.getMessage()));
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id , @RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id , @RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id , @RequestBody Map<String,String> tokenId) {
        try {
            Token token = authService.getToken(tokenId.get("token"), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
