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
import projet.projetstage02.exception.*;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
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
import static projet.projetstage02.utils.TimeUtil.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class RootController {
    final StudentService studentService;
    final CompanyService companyService;
    final GestionnaireService gestionnaireService;
    final AuthService authService;

    private final Logger logger = LogManager.getLogger(RootController.class);

    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        try {
            logger.log(Level.INFO, "PostMapping: /createStudent entered with body : " + studentDTO.toString());
            if (studentService.isStudentInvalid(studentDTO.getEmail())) {
                logger.log(Level.INFO, "PostMapping: /createStudent sent 409 response");
                return ResponseEntity.status(CONFLICT)
                        .body(getError("Cette adresse email est déjà utilisée."));
            }
            studentDTO.setInscriptionTimestamp(currentTimestamp());
            long id = studentService.saveStudent(studentDTO);
            studentDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(studentDTO.toModel())) {
                logger.log(Level.INFO, "PostMapping: /createCompany sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(Level.INFO, "PostMapping: /createStudent sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PostMapping: /createCompany sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        try {
            logger.log(Level.INFO, "Post /createCompany entered with body : " + companyDTO.toString());
            if (companyService.isCompanyInvalid(companyDTO.getEmail())) {
                logger.log(Level.INFO, "PostMapping: /createCompany sent 409 response");
                return ResponseEntity.status(CONFLICT)
                        .body(getError("Cette adresse email est déjà utilisée."));
            }
            companyDTO.setInscriptionTimestamp(currentTimestamp());
            long id = companyService.saveCompany(companyDTO);
            companyDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(companyDTO.toModel())) {
                logger.log(Level.INFO, "PostMapping: /createCompany sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(Level.INFO, "PostMapping: /createCompany sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PostMapping: /createCompany sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @SneakyThrows
    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        try {
            logger.log(Level.INFO, "Post /createGestionaire entered with body : " + gestionnaireDTO.toString());
            authService.getToken(gestionnaireDTO.getToken(), GESTIONNAIRE);
            if (gestionnaireService.isGestionnaireInvalid(gestionnaireDTO.getEmail())) {
                logger.log(Level.INFO, "PostMapping: /createGestionaire sent 409 response");
                return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
            }
            gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
            gestionnaireDTO.setConfirmed(true);
            long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
            gestionnaireDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel())) {
                logger.log(Level.INFO, "PostMapping: /createGestionaire sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(Level.INFO, "PostMapping: /createGestionaire sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PostMapping: /createGestionaire sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PostMapping: /createGestionaire sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@Valid @RequestBody OffreDTO offreDTO) {
        try {
            logger.log(Level.INFO, "Post /createOffre entered with body : " + offreDTO);
            Token token = authService.getToken(offreDTO.getToken(), COMPANY);
            offreDTO.setSession("Hiver " + getNextYear());
            offreDTO.setCompanyId(token.getUserId());
            companyService.createOffre(offreDTO);
            logger.log(Level.INFO, "PostMapping: /createOffre sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PostMapping: /createOffre sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
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
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            studentDTO.setEmailConfirmed(true);
            studentService.saveStudent(studentDTO);
            logger.log(Level.INFO, "PutMapping: /confirmEmail/student sent 201 response");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentEntityException e) {
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
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            logger.log(Level.INFO, "PutMapping: /confirmEmail/company sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /confirmEmail/company sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/confirmEmail/gestionaire/{id}")
    public ResponseEntity<Map<String, String>> confirmGestionnaireMail(@PathVariable String id) {
        try {
            GestionnaireDTO gestionnaireDTO = gestionnaireService.getGestionnaireById(Long.parseLong(id));
            if (currentTimestamp() - gestionnaireDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(Level.INFO, "PutMapping: /confirmEmail/gestionaire sent 400 response");
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            gestionnaireDTO.setEmailConfirmed(true);
            gestionnaireService.saveGestionnaire(gestionnaireDTO);
            logger.log(Level.INFO, "PutMapping: /confirmEmail/gestionaire sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /confirmEmail/gestionaire sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<TokenDTO> studentLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, STUDENT);
            logger.log(Level.INFO, "PostMapping: /student/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PostMapping: /student/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }

    }

    @PostMapping("/gestionnaire/login")
    public ResponseEntity<TokenDTO> gestionnaireLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, GESTIONNAIRE);
            logger.log(Level.INFO, "PostMapping: /gestionnaire/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PostMapping: /gestionnaire/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }

    }

    @PostMapping("/company/login")
    public ResponseEntity<TokenDTO> companyLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, COMPANY);
            logger.log(Level.INFO, "PostMapping: /company/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PostMapping: /company/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }

    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> getStudent(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /student entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), STUDENT);
            StudentDTO dto = studentService.getStudentById(token.getUserId());
            dto.setPassword("");
            logger.log(Level.INFO, "PutMapping: /student sent "
                    + (!dto.isEmailConfirmed() ? "404" : "200") + " response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /student sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /student sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /company entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), COMPANY);

            CompanyDTO dto = companyService.getCompanyById(token.getUserId());
            dto.setPassword("");
            logger.log(Level.INFO, !dto.isEmailConfirmed() ? "Put /company entered sent 400 response"
                    : "Put /company entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /company entered sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /company sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /gestionnaire entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            GestionnaireDTO dto = gestionnaireService.getGestionnaireById(token.getUserId());
            dto.setPassword("");
            logger.log(Level.INFO, !dto.isEmailConfirmed() ? "Put /gestionnaire entered sent 400 response"
                    : "Put /gestionnaire entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /gestionnaire sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /gestionnaire sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedStudents(@Valid @RequestBody TokenDTO tokenId) {
        try {
            logger.log(Level.INFO, "get /unvalidatedStudents entered");
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StudentDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();
            unvalidatedStudents.forEach(student -> student.setPassword(""));
            logger.log(Level.INFO, "PutMapping: /unvalidatedStudents sent 200 response");
            return ResponseEntity.ok(unvalidatedStudents);
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /unvalidatedStudents sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies(@Valid @RequestBody TokenDTO tokenId) {
        try {
            logger.log(Level.INFO, "get /unvalidatedCompanies entered");
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
            unvalidatedCompanies.forEach(company -> company.setPassword(""));
            logger.log(Level.INFO, "PutMapping: /unvalidatedCompanies sent 200 response");
            return ResponseEntity.ok(unvalidatedCompanies);
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /unvalidatedCompanies sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id,
                                                               @Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /validateStudent/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateStudent(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(Level.INFO, "PutMapping: /validateStudent sent 404 response");
            return ResponseEntity.status(NOT_FOUND)
                    .body(getError(exception.getMessage()));
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /validateStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id,
                                                               @Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /validateCompany/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateCompany(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(Level.INFO, "PutMapping: /validateCompany sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "PutMapping: /validateCompany sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id,
                                                             @Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Delete /removeStudent/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeStudent(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeStudent sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "DeleteMapping: /removeStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id,
                                                             @Valid @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Delete /removeCompany/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeCompany(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeCompany sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(Level.INFO, "DeleteMapping: /removeCompany sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedOffers")
    public ResponseEntity<List<OffreDTO>> getOfferToValidate(@RequestBody TokenDTO tokenDTO) {
        try {
            logger.log(Level.INFO, "put /unvalidatedOffers entered with year : ");
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            List<OffreDTO> unvalidatedOffers = gestionnaireService.getUnvalidatedOffers();
            logger.log(Level.INFO, "PutMapping: /unvalidatedOffers sent 200 response");
            return ResponseEntity.ok(unvalidatedOffers);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /unvalidatedOffers sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validatedOffers/{year}")
    public ResponseEntity<List<OffreDTO>> getValidatedOffersForYear(@PathVariable int year,
                                                                    @RequestBody TokenDTO tokenDTO) {
        try {
            logger.log(Level.INFO, "put /validatedOffers entered with year : " + year);
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            List<OffreDTO> validatedOffers = gestionnaireService.getValidatedOffers(year);
            logger.log(Level.INFO, "PutMapping: /validatedOffers sent 200 response");
            return ResponseEntity.ok(validatedOffers);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /validatedOffers sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateOffer/{id}")
    public ResponseEntity<OffreDTO> validateOffer(@PathVariable String id, @RequestBody TokenDTO tokenDTO) {
        logger.log(Level.INFO, "Put /validateOffer/{id} entered with id : " + id);
        try {
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            gestionnaireService.validateOfferById(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /validateOffer sent 200 response");
            OffreDTO offreDTO = gestionnaireService.validateOfferById(Long.parseLong(id));
            return ResponseEntity.ok(offreDTO);
        } catch (NonExistentOfferExeption | ExpiredSessionException exception) {
            logger.log(Level.INFO, "PutMapping: /validateOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /validateOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeOffer/{id}")
    public ResponseEntity<Map<String, String>> removeOffer(@PathVariable String id, @RequestBody TokenDTO tokenDTO) {
        logger.log(Level.INFO, "Delete /removeOffer/{id} entered with id : " + id);
        try {
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            gestionnaireService.removeOfferById(Long.parseLong(id));
            logger.log(Level.INFO, "DeleteMapping: /removeOffer sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            logger.log(Level.INFO, "DeleteMapping: /removeOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "DeleteMapping: /removeOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/uploadStudentCV")
    public ResponseEntity<StudentDTO> uploadStudentCurriculumVitae(@Valid @RequestBody PdfDTO pdf) {
        try {
            authService.getToken(pdf.getToken(), STUDENT);
            StudentDTO dto = studentService.uploadCurriculumVitae(pdf);
            dto.setPassword("");
            logger.log(Level.INFO, "PutMapping: /uploadStudentCV sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /uploadStudentCV sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /uploadStudentCV sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/offerPdf/{id}")
    public ResponseEntity<PdfOutDTO> getOfferPdf(@PathVariable String id, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /offerPdf/{id} entered with id : " + id);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO offerPdf = gestionnaireService.getOffrePdfById(Long.parseLong(id));
            logger.log(Level.INFO, "PutMapping: /offerPdf sent 200 response");
            return ResponseEntity.ok(offerPdf);
        } catch (NonExistentOfferExeption e) {
            logger.log(Level.INFO, "PutMapping: /offerPdf sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /offerPdf sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedCvStudents")
    public ResponseEntity<List<StudentDTO>> getUnvalidatedCvStudent(@RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /unvalidatedCvStudents entered with id : " + tokenId);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StudentDTO> students = gestionnaireService.getUnvalidatedCVStudents();
            logger.log(Level.INFO, "PutMapping: /unvalidatedCvStudents sent 200 response");
            return ResponseEntity.ok(students);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /unvalidatedCvStudents sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/studentCv/{studentId}")
    public ResponseEntity<PdfOutDTO> getStudentCv(@PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /studentCv entered with id : " + studentId);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO cv = gestionnaireService.getStudentCvToValidate(Long.parseLong(studentId));
            logger.log(Level.INFO, "PutMapping: /studentCv sent 200 response");
            return ResponseEntity.ok(cv);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /studentCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /studentCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateCv/{studentId}")
    public ResponseEntity<StudentDTO> validateStudentCv(@PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /validateCv entered with id : ");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            StudentDTO studentDTO = gestionnaireService.validateStudentCV(Long.parseLong(studentId));
            logger.log(Level.INFO, "PutMapping: /validateCv sent 200 response");
            return ResponseEntity.ok(studentDTO);
        } catch (NonExistentEntityException | InvalidStatusException e) {
            logger.log(Level.INFO, "PutMapping: /validateCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /validateCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/refuseCv/{studentId}")
    public ResponseEntity<StudentDTO> refuseStudentCv(@PathVariable long studentId, @RequestBody CvRefusalDTO cvRefusalDTO) {
        logger.log(Level.INFO, "Put /refuseCv entered with id : ");
        try {
            authService.getToken(cvRefusalDTO.getToken(), GESTIONNAIRE);
            StudentDTO studentDTO = gestionnaireService.removeStudentCvValidation(
                    studentId, cvRefusalDTO.getRefusalReason());
            logger.log(Level.INFO, "PutMapping: /refuseCv sent 200 response");
            return ResponseEntity.ok(studentDTO);
        } catch (NonExistentEntityException | InvalidStatusException e) {
            logger.log(Level.INFO, "PutMapping: /refuseCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /refuseCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getOffers/{studentId}")
    public ResponseEntity<List<OffreDTO>> getOffersByStudentDepartment(@PathVariable String studentId,
                                                                       @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /getOffersByStudentDepartment entered with student id : " + studentId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            List<OffreDTO> offers = studentService.getOffersByStudentDepartment(Long.parseLong(studentId));
            logger.log(Level.INFO, "PutMapping: /getOffersByStudentDepartment sent 200 response");
            return ResponseEntity.ok(offers);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "PutMapping: /getOffersByStudentDepartment sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "PutMapping: /getOffersByStudentDepartment sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getOfferStudent/{id}")
    public ResponseEntity<PdfOutDTO> getOfferStudent(@PathVariable String id, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /getOfferStudent entered with id : " + id);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            PdfOutDTO dto = studentService.getOfferPdfById(Long.parseLong(id));
            logger.log(Level.INFO, "Put /getOfferStudent sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /getOfferStudent sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /getOfferStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/applyToOffer/{studentId}_{offerId}")
    public ResponseEntity<ApplicationDTO> createPostulation
            (@PathVariable String studentId, @PathVariable String offerId, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /applyToOffer entered with id: " + studentId
                + " and offer id: " + offerId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            ApplicationDTO dto = studentService.createPostulation(Long.parseLong(studentId), Long.parseLong(offerId));
            logger.log(Level.INFO, "Put /applyToOffer sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /applyToOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /applyToOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (AlreadyExistingPostulation e) {
            logger.log(Level.INFO, "Put /applyToOffer sent 409 response");
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    @PutMapping("/studentApplys/{studentId}")
    public ResponseEntity<ApplicationListDTO> getPostulsOfferId(@PathVariable String studentId,
                                                                @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /getPostulsOfferId entered with id: " + studentId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            ApplicationListDTO dto = studentService.getPostulsOfferId(Long.parseLong(studentId));
            logger.log(Level.INFO, "Put /getPostulsOfferId sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /getPostulsOfferId sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /getPostulsOfferId sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/getStatutValidationCV/{id}")
    public ResponseEntity<CvStatusDTO> getStatutValidationCV(@PathVariable long id, @RequestBody TokenDTO tokenId) {

        logger.log(Level.INFO, "Put /getStatutValidationCV entered with id: " + id);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            CvStatusDTO dto = studentService.getStudentCvStatus(id);
            logger.log(Level.INFO, "Put /getStatutValidationCV sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /getStatutValidationCV sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /getStatutValidationCV sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/studentAcceptation/{offerId}_{studentId}")
    public ResponseEntity<ApplicationAcceptationDTO> saveStudentAcceptation
            (@PathVariable String offerId, @PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /studentAcceptation/{offerId}_{studentId} entered with offerId: " + offerId
                + " and studentId: " + studentId);

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            ApplicationAcceptationDTO dto = companyService
                    .saveStudentApplicationAccepted(Long.parseLong(offerId), Long.parseLong(studentId));
            logger.log(Level.INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (AlreadyExistingAcceptationException e) {
            logger.log(Level.INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 409 response");
            return ResponseEntity.status(CONFLICT).build();
        } catch (Exception e) {
            logger.log(Level.INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/getAcceptedStudentsForOffer/{offerId}")
    public ResponseEntity<OfferAcceptedStudentsDTO> getAcceptedStudentsForOffer
            (@PathVariable String offerId, @RequestBody TokenDTO tokenId) {
        logger.log(Level.INFO, "Put /getAcceptedStudentsForOffer/{offerId} entered with offerId: " + offerId);

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            OfferAcceptedStudentsDTO dto = companyService.getAcceptedStudentsForOffer(Long.parseLong(offerId));
            logger.log(Level.INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentOfferExeption e) {
            logger.log(Level.INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createStageContract")
    public ResponseEntity<StageContractOutDTO> createStageContract
            (@Valid StageContractInDTO stageContractInDTO, @RequestBody TokenDTO tokenId){
        logger.log(Level.INFO, "Post /createStageContract entered with StageContractInDTO: " + stageContractInDTO);

        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            StageContractOutDTO dto = gestionnaireService.createStageContract(stageContractInDTO);
            logger.log(Level.INFO, "Post /createStageContract sent request 201 : " + dto);
            return ResponseEntity.status(CREATED).body(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Post /createStageContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (NonExistentOfferExeption e) {
            logger.log(Level.INFO, "Post /createStageContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (AlreadyExistingStageContractException e) {
            logger.log(Level.INFO, "Post /createStageContract sent request 409");
            return ResponseEntity.status(CONFLICT).build();
        }catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Post /createStageContract sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/companySignatureContract")
    public ResponseEntity<StageContractOutDTO> companyContractSignature
            (@RequestBody SignatureInDTO signatureInDTO){
        logger.log(Level.INFO, "Put /companySignatureContract entered with SignatureInDTO: " + signatureInDTO);

        try {
            authService.getToken(signatureInDTO.getToken(), COMPANY);
            StageContractOutDTO dto = companyService.addSignatureToContract(signatureInDTO);
            logger.log(Level.INFO, "Put /companySignatureContract sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(Level.INFO, "Put /companySignatureContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /companySignatureContract sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (InvalidOwnershipException e) {
            logger.log(Level.INFO, "Put /companySignatureContract sent request 409");
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    @PutMapping("/unvalidatedAcceptations")
    public ResponseEntity<UnvalidatedAcceptationsDTO> getUnvalidatedAcceptations(@RequestBody TokenDTO tokenId){
        logger.log(Level.INFO, "Put /unvalidatedAcceptations");

        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            UnvalidatedAcceptationsDTO dto = gestionnaireService.getUnvalidatedAcceptation();
            logger.log(Level.INFO, "Put /unvalidatedAcceptations sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(Level.INFO, "Put /unvalidatedAcceptations sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }
}
