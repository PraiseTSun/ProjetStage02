package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.DTO.*;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;
import projet.projetstage02.utils.EmailUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static projet.projetstage02.model.Token.UserTypes.*;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

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
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
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
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
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
    public ResponseEntity<Map<String, String>> createGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        try{
            authService.getToken(gestionnaireDTO.getToken(), GESTIONNAIRE);
            if (!gestionnaireService.isEmailUnique(gestionnaireDTO.getEmail())) {
                return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
            }
            gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
            gestionnaireDTO.setConfirmed(true);
            long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
            gestionnaireDTO.setId(id);
            EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel());
            return ResponseEntity.status(CREATED).build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@Valid @RequestBody OffreDTO offreDTO){
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
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
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
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<TokenDTO> studentLogin(@RequestBody StudentDTO studentDTO){
        try {
            String token = authService.loginIfValid(studentDTO);
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        }catch (InvalidTokenException e){
            return ResponseEntity.status(403).build();
        }

    }

    @PostMapping("/gestionnaire/login")
    public ResponseEntity<TokenDTO> gestionnaireLogin(@RequestBody GestionnaireDTO gestionnaireDTO){
        try {
            String token = authService.loginIfValid(gestionnaireDTO);
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        }catch (InvalidTokenException e){
            return ResponseEntity.status(403).build();
        }

    }

    @PostMapping("/company/login")
    public ResponseEntity<TokenDTO> companyLogin(@RequestBody CompanyDTO companyDTO){
        try {
            String token = authService.loginIfValid(companyDTO);
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        }catch (InvalidTokenException e){
            return ResponseEntity.status(403).build();
        }

    }


    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), STUDENT);
            StudentDTO dto = studentService.getStudentById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), COMPANY);

            CompanyDTO dto = companyService.getCompanyById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            GestionnaireDTO dto = gestionnaireService.getGestionnaireById(token.getUserId());
            dto.setPassword("");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents(@Valid @RequestBody TokenDTO tokenId) {
        try{
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            List<StudentDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();
            unvalidatedStudents.forEach(student -> student.setPassword(""));
            return ResponseEntity.ok(unvalidatedStudents);
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies(@Valid @RequestBody TokenDTO tokenId)  {
        try{
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
            unvalidatedCompanies.forEach(company -> company.setPassword(""));
            return ResponseEntity.ok(unvalidatedCompanies);
        }catch (NonExistentEntityException e){
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id , @Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getError(exception.getMessage()));
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id ,@Valid  @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id ,@Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeStudent(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id ,@Valid @RequestBody TokenDTO tokenId) {
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeCompany(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            return ResponseEntity.notFound().build();
        }catch (InvalidTokenException ex){
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/unvalidatedOffers")
    public ResponseEntity<List<OffreDTO>> getOfferToValidate() {
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
