package projet.projetstage02.controller;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.cv.CvRefusalDTO;
import projet.projetstage02.dto.evaluations.EvaluationInfoDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.notification.GestionnaireNotificationDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.utils.EmailUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.Level.INFO;
import static org.springframework.http.HttpStatus.*;
import static projet.projetstage02.model.Token.UserTypes.GESTIONNAIRE;
import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class GestionnaireRootController {
    final GestionnaireService gestionnaireService;
    final AuthService authService;

    private final Logger logger = LogManager.getLogger(RootController.class);


    private Map<String, String> getError(String error) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("error", error);
        return hashMap;
    }

    @SneakyThrows
    @PostMapping("/createGestionnaire")
    public ResponseEntity<Map<String, String>> createGestionnaire(@Valid @RequestBody GestionnaireDTO gestionnaireDTO) {
        try {
            logger.log(INFO, "Post /createGestionaire entered with body : " + gestionnaireDTO.toString());
            authService.getToken(gestionnaireDTO.getToken(), GESTIONNAIRE);
            if (gestionnaireService.isGestionnaireInvalid(gestionnaireDTO.getEmail())) {
                logger.log(INFO, "PostMapping: /createGestionaire sent 409 response");
                return ResponseEntity.status(CONFLICT).body(getError("Cette adresse email est déjà utilisée."));
            }
            gestionnaireDTO.setInscriptionTimestamp(currentTimestamp());
            gestionnaireDTO.setConfirmed(true);
            long id = gestionnaireService.saveGestionnaire(gestionnaireDTO);
            gestionnaireDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(gestionnaireDTO.toModel())) {
                logger.log(INFO, "PostMapping: /createGestionaire sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(INFO, "PostMapping: /createGestionaire sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PostMapping: /createGestionaire sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PostMapping: /createGestionaire sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @PutMapping("/confirmEmail/gestionaire/{id}")
    public ResponseEntity<Map<String, String>> confirmGestionnaireMail(@PathVariable String id) {
        try {
            GestionnaireDTO gestionnaireDTO = gestionnaireService.getGestionnaireById(Long.parseLong(id));
            if (currentTimestamp() - gestionnaireDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(INFO, "PutMapping: /confirmEmail/gestionaire sent 400 response");
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            gestionnaireDTO.setEmailConfirmed(true);
            gestionnaireService.saveGestionnaire(gestionnaireDTO);
            logger.log(INFO, "PutMapping: /confirmEmail/gestionaire sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /confirmEmail/gestionaire sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/gestionnaire")
    public ResponseEntity<GestionnaireDTO> getGestionnaire(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /gestionnaire entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            GestionnaireDTO dto = gestionnaireService.getGestionnaireById(token.getUserId());
            dto.setPassword("");
            logger.log(INFO, !dto.isEmailConfirmed() ? "Put /gestionnaire entered sent 400 response"
                    : "Put /gestionnaire entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /gestionnaire sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /gestionnaire sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedStudents")
    public ResponseEntity<List<StudentOutDTO>> getUnvalidatedStudents(@Valid @RequestBody TokenDTO tokenId) {
        try {
            logger.log(INFO, "get /unvalidatedStudents entered");
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StudentOutDTO> unvalidatedStudents =
                    gestionnaireService.getUnvalidatedStudents();
            unvalidatedStudents.forEach(student -> student.setPassword(""));
            logger.log(INFO, "PutMapping: /unvalidatedStudents sent 200 response");
            return ResponseEntity.ok(unvalidatedStudents);
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /unvalidatedStudents sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedCompanies")
    public ResponseEntity<List<CompanyDTO>> getUnvalidatedCompanies(@Valid @RequestBody TokenDTO tokenId) {
        try {
            logger.log(INFO, "get /unvalidatedCompanies entered");
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();
            unvalidatedCompanies.forEach(company -> company.setPassword(""));
            logger.log(INFO, "PutMapping: /unvalidatedCompanies sent 200 response");
            return ResponseEntity.ok(unvalidatedCompanies);
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /unvalidatedCompanies sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateStudent/{id}")
    public ResponseEntity<Map<String, String>> validateStudent(@PathVariable String id,
                                                               @Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /validateStudent/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateStudent(Long.parseLong(id));
            logger.log(INFO, "PutMapping: /validateStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(INFO, "PutMapping: /validateStudent sent 404 response");
            return ResponseEntity.status(NOT_FOUND)
                    .body(getError(exception.getMessage()));
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /validateStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateCompany/{id}")
    public ResponseEntity<Map<String, String>> validateCompany(@PathVariable String id,
                                                               @Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /validateCompany/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.validateCompany(Long.parseLong(id));
            logger.log(INFO, "PutMapping: /validateCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(INFO, "PutMapping: /validateCompany sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /validateCompany sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeStudent/{id}")
    public ResponseEntity<Map<String, String>> removeStudent(@PathVariable String id,
                                                             @Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Delete /removeStudent/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeStudent(Long.parseLong(id));
            logger.log(INFO, "DeleteMapping: /removeStudent sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(INFO, "DeleteMapping: /removeStudent sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "DeleteMapping: /removeStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeCompany/{id}")
    public ResponseEntity<Map<String, String>> removeCompany(@PathVariable String id,
                                                             @Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Delete /removeCompany/{id} entered with id : " + id);
        try {
            Token token = authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            gestionnaireService.getGestionnaireById(token.getUserId());
            gestionnaireService.removeCompany(Long.parseLong(id));
            logger.log(INFO, "DeleteMapping: /removeCompany sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentEntityException exception) {
            logger.log(INFO, "DeleteMapping: /removeCompany sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "DeleteMapping: /removeCompany sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedOffers")
    public ResponseEntity<List<OffreOutDTO>> getOfferToValidate(@RequestBody TokenDTO tokenDTO) {
        try {
            logger.log(INFO, "put /unvalidatedOffers entered with year : ");
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            List<OffreOutDTO> unvalidatedOffers = gestionnaireService.getUnvalidatedOffers();
            logger.log(INFO, "PutMapping: /unvalidatedOffers sent 200 response");
            return ResponseEntity.ok(unvalidatedOffers);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /unvalidatedOffers sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validatedOffers/{year}")
    public ResponseEntity<List<OffreOutDTO>> getValidatedOffersForYear(@PathVariable int year,
                                                                       @RequestBody TokenDTO tokenDTO) {
        try {
            logger.log(INFO, "put /validatedOffers entered with year : " + year);
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            List<OffreOutDTO> validatedOffers = gestionnaireService.getValidatedOffers(year);
            logger.log(INFO, "PutMapping: /validatedOffers sent 200 response");
            return ResponseEntity.ok(validatedOffers);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /validatedOffers sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateOffer/{id}")
    public ResponseEntity<OffreOutDTO> validateOffer(@PathVariable String id, @RequestBody TokenDTO tokenDTO) {
        logger.log(INFO, "Put /validateOffer/{id} entered with id : " + id);
        try {
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            gestionnaireService.validateOfferById(Long.parseLong(id));
            logger.log(INFO, "PutMapping: /validateOffer sent 200 response");
            OffreOutDTO offreInDTO = gestionnaireService.validateOfferById(Long.parseLong(id));
            return ResponseEntity.ok(offreInDTO);
        } catch (NonExistentOfferExeption | ExpiredSessionException exception) {
            logger.log(INFO, "PutMapping: /validateOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /validateOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @DeleteMapping("/removeOffer/{id}")
    public ResponseEntity<Map<String, String>> removeOffer(@PathVariable String id, @RequestBody TokenDTO tokenDTO) {
        logger.log(INFO, "Delete /removeOffer/{id} entered with id : " + id);
        try {
            authService.getToken(tokenDTO.getToken(), GESTIONNAIRE);
            gestionnaireService.removeOfferById(Long.parseLong(id));
            logger.log(INFO, "DeleteMapping: /removeOffer sent 200 response");
            return ResponseEntity.ok().build();
        } catch (NonExistentOfferExeption exception) {
            logger.log(INFO, "DeleteMapping: /removeOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "DeleteMapping: /removeOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/offerPdf/{id}")
    public ResponseEntity<PdfOutDTO> getOfferPdf(@PathVariable String id, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /offerPdf/{id} entered with id : " + id);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO offerPdf = gestionnaireService.getOffrePdfById(Long.parseLong(id));
            logger.log(INFO, "PutMapping: /offerPdf sent 200 response");
            return ResponseEntity.ok(offerPdf);
        } catch (NonExistentOfferExeption e) {
            logger.log(INFO, "PutMapping: /offerPdf sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /offerPdf sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/unvalidatedCvStudents")
    public ResponseEntity<List<StudentOutDTO>> getUnvalidatedCvStudent(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /unvalidatedCvStudents entered with id : " + tokenId);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StudentOutDTO> students = gestionnaireService.getUnvalidatedCVStudents();
            logger.log(INFO, "PutMapping: /unvalidatedCvStudents sent 200 response");
            return ResponseEntity.ok(students);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /unvalidatedCvStudents sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/studentCv/{studentId}")
    public ResponseEntity<PdfOutDTO> getStudentCv(@PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /studentCv entered with id : " + studentId);
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO cv = gestionnaireService.getStudentCvToValidate(Long.parseLong(studentId));
            logger.log(INFO, "PutMapping: /studentCv sent 200 response");
            return ResponseEntity.ok(cv);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /studentCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/validateCv/{studentId}")
    public ResponseEntity<StudentOutDTO> validateStudentCv(@PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /validateCv entered with id : ");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            StudentOutDTO studentDTO = gestionnaireService.validateStudentCV(Long.parseLong(studentId));
            logger.log(INFO, "PutMapping: /validateCv sent 200 response");
            return ResponseEntity.ok(studentDTO);
        } catch (NonExistentEntityException | InvalidStatusException e) {
            logger.log(INFO, "PutMapping: /validateCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /validateCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/refuseCv/{studentId}")
    public ResponseEntity<StudentOutDTO> refuseStudentCv(@PathVariable long studentId, @RequestBody CvRefusalDTO cvRefusalDTO) {
        logger.log(INFO, "Put /refuseCv entered with id : " + studentId);
        try {
            authService.getToken(cvRefusalDTO.getToken(), GESTIONNAIRE);
            StudentOutDTO studentDTO = gestionnaireService.removeStudentCvValidation(
                    studentId, cvRefusalDTO.getRefusalReason());
            logger.log(INFO, "PutMapping: /refuseCv sent 200 response");
            return ResponseEntity.ok(studentDTO);
        } catch (NonExistentEntityException | InvalidStatusException e) {
            logger.log(INFO, "PutMapping: /refuseCv sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /refuseCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    //todo replace .log with .info
    @PostMapping("/createStageContract")
    public ResponseEntity<StageContractOutDTO> createStageContract
    (@Valid @RequestBody StageContractInDTO stageContractInDTO) {
        logger.log(INFO, "Post /createStageContract entered with StageContractInDTO: " + stageContractInDTO);
        try {
            authService.getToken(stageContractInDTO.getToken(), GESTIONNAIRE);
            StageContractOutDTO dto = gestionnaireService.createStageContract(stageContractInDTO);
            logger.log(INFO, "Post /createStageContract sent request 201 : " + dto);
            return ResponseEntity.status(CREATED).body(dto);
        } catch (NonExistentEntityException | NonExistentOfferExeption e) {
            logger.log(INFO, "Post /createStageContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (AlreadyExistingStageContractException e) {
            logger.log(INFO, "Post /createStageContract sent request 409");
            return ResponseEntity.status(CONFLICT).build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Post /createStageContract sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/contractsToCreate")
    public ResponseEntity<ContractsDTO> getContracts(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /contractsToCreate");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            ContractsDTO dto = gestionnaireService.getContractsToCreate();
            logger.log(INFO, "Put /contractsToCreate sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /contractsToCreate sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/evaluateStage/{contractId}/getInfo")
    public ResponseEntity<EvaluationInfoDTO> getEvaluateStageInfo
            (@PathVariable long contractId, @RequestBody TokenDTO tokenId) {
        try {
            logger.log(INFO, "put /evaluateStage/id/getInfo entered with id : " + contractId);
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            EvaluationInfoDTO eval =
                    gestionnaireService.getEvaluationInfoForContract(contractId);
            logger.log(INFO, "PutMapping: /evaluateStage/id/getInfo sent 201 response");
            return ResponseEntity.status(CREATED).body(eval);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id/getInfo sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException | NonExistentOfferExeption e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id/getInfo sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/evaluateStage/{token}")
    public ResponseEntity<?> evaluateStage
            (@PathVariable String token, @RequestBody MillieuStageEvaluationInDTO millieuStageEvaluationInDTO) {
        try {
            logger.log(INFO, "put /evaluateStage/id entered with id : " + token);
            authService.getToken(token, GESTIONNAIRE);
            gestionnaireService.evaluateStage(millieuStageEvaluationInDTO);
            gestionnaireService.createEvaluationMillieuStagePDF(millieuStageEvaluationInDTO.getContractId());
            logger.log(INFO, "PutMapping: /evaluateStage/id sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentOfferExeption | NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id sent 404 response");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (EmptySignatureException e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id sent 401 response");
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (DocumentException e) {
            logger.log(INFO, "PutMapping: /evaluateStage/id sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/getContractsToEvaluate/millieuStage")
    public ResponseEntity<ContractsDTO> getAllContracts(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getContracts");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            ContractsDTO dto = gestionnaireService.getContractsToEvaluateMillieuStage();
            logger.log(INFO, "Put /getContracts sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getContracts sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getEvaluationPDF/millieuStage/{id}")
    public ResponseEntity<PdfOutDTO> getEvaluationMillieuStagePDF(@PathVariable long id, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getEvaluationPDF/millieuStage/{id}");

        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO dto = gestionnaireService.getEvaluationMillieuStagePDF(id);
            logger.log(INFO, "Put /getEvaluationPDF/millieuStage/{id} return 200");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getEvaluationPDF/millieuStage/{id} return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getEvaluationPDF/millieuStage/{id} return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getEvaluatedContracts/millieuStage")
    public ResponseEntity<List<StageContractOutDTO>> getEvaluatedContractsMillieuStage(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getEvaluatedContracts/millieuStage");

        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StageContractOutDTO> evaluatedContracts = gestionnaireService.getEvaluationMillieuStage();
            logger.log(INFO, "Put /getEvaluatedContracts/millieuStage return 200");
            return ResponseEntity.ok(evaluatedContracts);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getEvaluatedContracts/millieuStage return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getGestionnaireContracts")
    public ResponseEntity<List<StageContractOutDTO>> GetGestionnaireContract(@RequestBody TokenDTO token) {
        logger.log(INFO, "Put getGestionnaireContracts");
        try {
            authService.getToken(token.getToken(), GESTIONNAIRE);
            List<StageContractOutDTO> contracts = gestionnaireService.getContractsToSigne();
            logger.log(INFO, "Put getGestionnaireContracts return 200");
            return ResponseEntity.ok(contracts);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put getGestionnaireContracts return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/gestionnaireSignature")
    public ResponseEntity<StageContractOutDTO> GestionnaireSignature(@RequestBody SignatureInDTO signature) {
        logger.log(INFO, "Put gestionnaireSignature");

        try {
            authService.getToken(signature.getToken(), GESTIONNAIRE);
            StageContractOutDTO dto = gestionnaireService.contractSignature(signature);
            logger.log(INFO, "Put gestionnaireSignature return 200");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put gestionnaireSignature return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put gestionnaireSignature return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (NotReadyToBeSignedException e) {
            logger.log(INFO, "Put gestionnaireSignature return 409");
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    @PutMapping("/getEvaluatedContracts/etudiants")
    public ResponseEntity<List<StageContractOutDTO>> getEvaluatedContractsEtudiants(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getEvaluatedContracts/etudiants");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            List<StageContractOutDTO> dto = gestionnaireService.getEvaluatedContractsEtudiants();
            logger.log(INFO, "Put /getEvaluatedContracts/etudiants sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getEvaluatedContracts/etudiants sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getEvaluationPDF/etudiant/{id}")
    public ResponseEntity<PdfOutDTO> getEvaluationPDFEtudiant(@PathVariable long id, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getEvaluationPDF/etudiant/{id}");
        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            PdfOutDTO pdf = gestionnaireService.getEvaluationPDFEtudiant(id);
            logger.log(INFO, "Put /getEvaluationPDF/etudiant/{id} sent request 200");
            return ResponseEntity.ok(pdf);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getEvaluationPDF/etudiant/{id} sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getEvaluationPDF/etudiant/{id} sent request 404");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/getGestionnaireNotification")
    public ResponseEntity<GestionnaireNotificationDTO> getGestionnaireNotification(@RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getGestionnaireNotification");

        try {
            authService.getToken(tokenId.getToken(), GESTIONNAIRE);
            GestionnaireNotificationDTO notification = gestionnaireService.getNotification();
            logger.log(INFO, "Put /getGestionnaireNotification return 200");
            return ResponseEntity.ok(notification);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getGestionnaireNotification return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }
}
