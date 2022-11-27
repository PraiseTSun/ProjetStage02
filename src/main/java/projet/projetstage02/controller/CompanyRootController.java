package projet.projetstage02.controller;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.applications.ApplicationAcceptationDTO;
import projet.projetstage02.dto.applications.OfferApplicationDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.interview.CreateInterviewDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.notification.CompanyNotificationDTO;
import projet.projetstage02.dto.offres.OfferAcceptedStudentsDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.utils.EmailUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.Level.INFO;
import static org.springframework.http.HttpStatus.*;
import static projet.projetstage02.model.Token.UserTypes.COMPANY;
import static projet.projetstage02.model.Token.UserTypes.STUDENT;
import static projet.projetstage02.utils.TimeUtil.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class CompanyRootController {
    final CompanyService companyService;
    final GestionnaireService gestionnaireService;
    final AuthService authService;

    private final Logger logger = LogManager.getLogger(RootController.class);

    private Map<String, String> getError(String error) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("error", error);
        return hashMap;
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        try {
            logger.log(INFO, "Post /createCompany entered with body : " + companyDTO.toString());
            if (companyService.isCompanyInvalid(companyDTO.getEmail())) {
                logger.log(INFO, "PostMapping: /createCompany sent 409 response");
                return ResponseEntity.status(CONFLICT)
                        .body(getError("Cette adresse email est déjà utilisée."));
            }
            companyDTO.setInscriptionTimestamp(currentTimestamp());
            long id = companyService.saveCompany(companyDTO);
            companyDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(companyDTO.toModel())) {
                logger.log(INFO, "PostMapping: /createCompany sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(INFO, "PostMapping: /createCompany sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PostMapping: /createCompany sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @PostMapping("/createOffre")
    public ResponseEntity<Map<String, String>> createOffre(@Valid @RequestBody OffreInDTO offreInDTO) {
        try {
            logger.log(INFO, "Post /createOffre entered with body : " + offreInDTO);
            Token token = authService.getToken(offreInDTO.getToken(), COMPANY);
            offreInDTO.setSession("Hiver " + getNextYear());
            offreInDTO.setCompanyId(token.getUserId());
            companyService.createOffre(offreInDTO);
            logger.log(INFO, "PostMapping: /createOffre sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PostMapping: /createOffre sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/confirmEmail/company/{id}")
    public ResponseEntity<Map<String, String>> confirmCompanyMail(@PathVariable String id) {
        logger.log(INFO, "Put /confirmEmail/company/{id} entered with id : " + id);
        try {
            CompanyDTO companyDTO = companyService.getCompanyByIdDTO(Long.parseLong(id));
            if (currentTimestamp() - companyDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(INFO, "PutMapping: /confirmEmail/company sent 400 response");
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            companyDTO.setEmailConfirmed(true);
            companyService.saveCompany(companyDTO);
            logger.log(INFO, "PutMapping: /confirmEmail/company sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /confirmEmail/company sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyDTO> getCompany(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /company entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), COMPANY);
            CompanyDTO dto = companyService.getCompanyByIdDTO(token.getUserId());
            dto.setPassword("");
            logger.log(INFO, !dto.isEmailConfirmed() ? "Put /company entered sent 400 response"
                    : "Put /company entered sent 200 response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /company entered sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /company sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/company/studentCv/{studentId}")
    private ResponseEntity<PdfOutDTO> getCvAsCompany(@PathVariable long studentId, @RequestBody TokenDTO tokenId) {
        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            PdfOutDTO cv = companyService.getStudentCv(studentId);
            logger.log(INFO, "PutMapping: /studentCv sent 200 response");
            return ResponseEntity.ok(cv);
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /studentCv sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/studentAcceptation/{offerId}_{studentId}")
    public ResponseEntity<ApplicationAcceptationDTO> saveStudentAcceptation
            (@PathVariable String offerId, @PathVariable String studentId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} entered with offerId: " + offerId
                + " and studentId: " + studentId);

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            ApplicationAcceptationDTO dto = companyService
                    .saveStudentApplicationAccepted(Long.parseLong(offerId), Long.parseLong(studentId));
            logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (AlreadyExistingAcceptationException e) {
            logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 409 response");
            return ResponseEntity.status(CONFLICT).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (NonExistentOfferExeption e) {
            logger.log(INFO, "Put /studentAcceptation/{offerId}_{studentId} sent 404 response : ");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/getAcceptedStudentsForOffer/{offerId}")
    public ResponseEntity<OfferAcceptedStudentsDTO> getAcceptedStudentsForOffer
            (@PathVariable String offerId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getAcceptedStudentsForOffer/{offerId} entered with offerId: " + offerId);

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            OfferAcceptedStudentsDTO dto = companyService.getAcceptedStudentsForOffer(Long.parseLong(offerId));
            logger.log(INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getAcceptedStudentsForOffer/{offerId} sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/companyContracts/{companyId}_{session}")
    public ResponseEntity<ContractsDTO> getCompanyContracts
            (@PathVariable String companyId, @PathVariable String session, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /companyContracts/{companyId} entered with companyId: " + companyId
                + " with the session: " + session);

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            ContractsDTO contracts = companyService.getContracts(Long.parseLong(companyId), session);
            logger.log(INFO, "Put /companyContracts/{companyId} sent 200 response");
            return ResponseEntity.ok(contracts);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /companyContracts/{companyId}_{session} sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /companyContracts/{companyId}_{session} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/companySignatureContract")
    public ResponseEntity<StageContractOutDTO> companyContractSignature
            (@Valid @RequestBody SignatureInDTO signatureInDTO) {
        logger.log(INFO, "Put /companySignatureContract entered with SignatureInDTO: " + signatureInDTO);

        try {
            authService.getToken(signatureInDTO.getToken(), COMPANY);
            StageContractOutDTO dto = companyService.addSignatureToContract(signatureInDTO);
            logger.log(INFO, "Put /companySignatureContract sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /companySignatureContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException | InvalidOwnershipException e) {
            logger.log(INFO, "Put /companySignatureContract sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/offer/{id}/applications")
    public ResponseEntity<OfferApplicationDTO> getStudentForOffer
            (@PathVariable long id, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "/offer/{id}/applications entered with id: " + id);
        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            OfferApplicationDTO dto = companyService.getStudentsForOffer(id);
            logger.log(INFO, "/offer/{id}/applications sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "/offer/{id}/applications sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "/offer/{id}/applications sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/company/validatedOffers/{id}")
    public ResponseEntity<List<OffreOutDTO>> getValidatedOffers
            (@PathVariable long id, @RequestBody TokenDTO tokenId) {
        try {
            logger.log(INFO, "put /validatedOffers entered with id : " + id);
            authService.getToken(tokenId.getToken(), COMPANY);
            List<OffreOutDTO> validatedOffers = companyService.getValidatedOffers(id);
            logger.log(INFO, "PutMapping: /validatedOffers sent 200 response");
            return ResponseEntity.ok(validatedOffers);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /validatedOffers sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PostMapping("/createInterview")
    public ResponseEntity<InterviewOutDTO> createInterview(@RequestBody @Valid CreateInterviewDTO interviewDTO) {
        logger.log(INFO, "Post /createInterview");

        try {
            authService.getToken(interviewDTO.getToken(), COMPANY);
            InterviewOutDTO dto = companyService.createInterview(interviewDTO);
            logger.log(INFO, "Post /createInterview return 201 request");
            return ResponseEntity.status(CREATED).body(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Post /createInterview return 404 request");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (InvalidDateFormatException e) {
            logger.log(INFO, "Post /createInterview return 400 request");
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (InvalidTokenException | InvalidOwnershipException e) {
            logger.log(INFO, "Post /createInterview return 403 request");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getCompanyInterviews/{companyId}")
    public ResponseEntity<List<InterviewOutDTO>> getCompanyInterviews
            (@PathVariable String companyId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getCompanyInterviews/{companyId}");

        try {
            authService.getToken(tokenId.getToken(), COMPANY);
            List<InterviewOutDTO> interviews = companyService.getInterviews(Long.parseLong(companyId));
            logger.log(INFO, "Put /getCompanyInterviews/{companyId} return 200");
            return ResponseEntity.ok(interviews);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getCompanyInterviews/{companyId} return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getCompanyInterviews/{companyId} return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getEvaluatedStudentsContracts/{companyId}")
    public ResponseEntity<List<Long>> getEvaluatedStudents(@PathVariable long companyId, @RequestBody TokenDTO token) {
        try {
            logger.log(INFO, "Put /getEvaluatedStudents entered");
            authService.getToken(token.getToken(), COMPANY);
            List<Long> contractIds = companyService.getEvaluatedStudentsContracts(companyId);
            logger.log(INFO, "Put /getEvaluatedStudents sent 200 response");
            return ResponseEntity.ok(contractIds);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getEvaluatedStudents sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }


    @PostMapping("/evaluateStudent/{token}")
    public ResponseEntity<?> evaluateStudent(@PathVariable String token, @RequestBody @Valid EvaluationEtudiantInDTO studentEvaluationInDTO) {
        try {
            logger.log(INFO, "put /evaluateStudent entered");
            authService.getToken(token, COMPANY);
            companyService.evaluateStudent(studentEvaluationInDTO);
            gestionnaireService.createEvaluationEtudiantPDF(studentEvaluationInDTO.getContractId());
            logger.log(INFO, "PutMapping: /evaluateStudent sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /evaluateStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentOfferExeption | NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /evaluateStudent sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (EmptySignatureException e) {
            logger.log(INFO, "PutMapping: /evaluateStudent sent 401 response");
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (DocumentException e) {
            logger.log(INFO, "PutMapping: /evaluateStudent sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/companyNotification/{companyId}")
    public ResponseEntity<CompanyNotificationDTO> getCompanyNotification
            (@PathVariable String companyId, @RequestBody TokenDTO token) {
        logger.log(INFO, "Put /companyNotification/{companyId} with the id: " + companyId);

        try {
            authService.getToken(token.getToken(), COMPANY);
            CompanyNotificationDTO notification = companyService.getNotification(Long.parseLong(companyId));
            logger.log(INFO, "Put /companyNotification/{companyId} return 200");
            return ResponseEntity.ok(notification);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /companyNotification/{companyId} return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /companyNotification/{companyId} return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }
}