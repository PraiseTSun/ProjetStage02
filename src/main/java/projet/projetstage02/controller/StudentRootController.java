package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.applications.ApplicationDTO;
import projet.projetstage02.dto.applications.ApplicationListDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.cv.CvStatusDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.interview.InterviewSelectInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.StudentService;
import projet.projetstage02.utils.EmailUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.Level.INFO;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static projet.projetstage02.model.Token.UserTypes.STUDENT;
import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class StudentRootController {
    final StudentService studentService;
    final AuthService authService;

    private final Logger logger = LogManager.getLogger(RootController.class);

    private Map<String, String> getError(String error) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("error", error);
        return hashMap;
    }

    @PostMapping("/createStudent")
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody StudentInDTO studentDTO) {
        try {
            logger.log(INFO, "PostMapping: /createStudent entered with body : " + studentDTO.toString());
            if (studentService.isStudentInvalid(studentDTO.getEmail())) {
                logger.log(INFO, "PostMapping: /createStudent sent 409 response");
                return ResponseEntity.status(CONFLICT)
                        .body(getError("Cette adresse email est déjà utilisée."));
            }
            studentDTO.setInscriptionTimestamp(currentTimestamp());
            long id = studentService.saveStudent(studentDTO);
            studentDTO.setId(id);
            if (!EmailUtil.sendConfirmationMail(studentDTO.toModel())) {
                logger.log(INFO, "PostMapping: /createCompany sent 500 response");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                        getError("Une erreur avec le service d'email est survenue"));
            }
            logger.log(INFO, "PostMapping: /createStudent sent 201 response");
            return ResponseEntity.status(CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PostMapping: /createCompany sent 500 response");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    getError("Une erreur est survenue"));
        }
    }

    @PutMapping("/confirmEmail/student/{id}")
    public ResponseEntity<Map<String, String>> confirmStudentMail(@PathVariable String id) {
        logger.log(INFO, "Put /confirmEmail/student/{id} entered with id : " + id);
        try {
            StudentOutDTO studentDTO = studentService.getStudentById(Long.parseLong(id));
            if (currentTimestamp() - studentDTO.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
                logger.log(INFO, "PutMapping: /confirmEmail/student sent 400 response");
                return ResponseEntity.status(BAD_REQUEST)
                        .body(getError("La période de confirmation est expirée"));
            }
            studentDTO.setEmailConfirmed(true);
            studentService.saveStudent(new StudentInDTO(studentDTO.toModel()));
            logger.log(INFO, "PutMapping: /confirmEmail/student sent 201 response");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /confirmEmail/student sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student")
    public ResponseEntity<StudentOutDTO> getStudent(@Valid @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /student entered with body : " + tokenId.toString());
        try {
            Token token = authService.getToken(tokenId.getToken(), STUDENT);
            StudentOutDTO dto = studentService.getStudentById(token.getUserId());
            dto.setPassword("");
            logger.log(INFO, "PutMapping: /student sent "
                    + (!dto.isEmailConfirmed() ? "404" : "200") + " response");
            return !dto.isEmailConfirmed() ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /student sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException ex) {
            logger.log(INFO, "PutMapping: /student sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/uploadStudentCV")
    public ResponseEntity<StudentOutDTO> uploadStudentCurriculumVitae(@Valid @RequestBody PdfDTO pdf) {
        try {
            authService.getToken(pdf.getToken(), STUDENT);
            StudentOutDTO dto = studentService.uploadCurriculumVitae(pdf);
            dto.setPassword("");
            logger.log(INFO, "PutMapping: /uploadStudentCV sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /uploadStudentCV sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /uploadStudentCV sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getOffers/{studentId}")
    public ResponseEntity<List<OffreOutDTO>> getOffersByStudentDepartment(@PathVariable String studentId,
                                                                          @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getOffersByStudentDepartment entered with student id : " + studentId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            List<OffreOutDTO> offers = studentService.getOffersByStudentDepartment(Long.parseLong(studentId));
            logger.log(INFO, "PutMapping: /getOffersByStudentDepartment sent 200 response");
            return ResponseEntity.ok(offers);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "PutMapping: /getOffersByStudentDepartment sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PutMapping: /getOffersByStudentDepartment sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/getOfferStudent/{id}")
    public ResponseEntity<PdfOutDTO> getOfferStudent(@PathVariable String id, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getOfferStudent entered with id : " + id);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            PdfOutDTO dto = studentService.getOfferPdfById(Long.parseLong(id));
            logger.log(INFO, "Put /getOfferStudent sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getOfferStudent sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getOfferStudent sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/applyToOffer/{studentId}_{offerId}")
    public ResponseEntity<ApplicationDTO> createPostulation
            (@PathVariable String studentId, @PathVariable String offerId, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /applyToOffer entered with id: " + studentId
                + " and offer id: " + offerId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            ApplicationDTO dto = studentService.createPostulation(Long.parseLong(studentId), Long.parseLong(offerId));
            logger.log(INFO, "Put /applyToOffer sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /applyToOffer sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /applyToOffer sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (AlreadyExistingPostulation e) {
            logger.log(INFO, "Put /applyToOffer sent 409 response");
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    @PutMapping("/studentApplys/{studentId}")
    public ResponseEntity<ApplicationListDTO> getPostulsOfferId(@PathVariable String studentId,
                                                                @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /getPostulsOfferId entered with id: " + studentId);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            ApplicationListDTO dto = studentService.getPostulsOfferId(Long.parseLong(studentId));
            logger.log(INFO, "Put /getPostulsOfferId sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getPostulsOfferId sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getPostulsOfferId sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/getStatutValidationCV/{id}")
    public ResponseEntity<CvStatusDTO> getStatutValidationCV(@PathVariable long id, @RequestBody TokenDTO tokenId) {

        logger.log(INFO, "Put /getStatutValidationCV entered with id: " + id);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            CvStatusDTO dto = studentService.getStudentCvStatus(id);
            logger.log(INFO, "Put /getStatutValidationCV sent 200 response");
            return ResponseEntity.ok(dto);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /getStatutValidationCV sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /getStatutValidationCV sent 404 response");
            return ResponseEntity.notFound().build();
        }
    }

    //Todo replace contractsDTO with list of stageContract
    @PutMapping("/studentContracts/{studentId}_{session}")
    public ResponseEntity<List<StageContractOutDTO>> getStudentContracts
    (@PathVariable String studentId, @PathVariable String session, @RequestBody TokenDTO tokenId) {
        logger.log(INFO, "Put /studentContracts/{studentId}_{session} entered with studentId: " + studentId
                + " with the session: " + session);

        try {
            authService.getToken(tokenId.getToken(), STUDENT);
            List<StageContractOutDTO> contracts = studentService.getContracts(Long.parseLong(studentId), session);
            logger.log(INFO, "Put /studentContracts/{studentId}_{session} sent 200 response");
            return ResponseEntity.ok(contracts);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /studentContracts/{studentId}_{session} sent 404 response");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /studentContracts/{studentId}_{session} sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PutMapping("/studentSignatureContract")
    public ResponseEntity<StageContractOutDTO> studentContractSignature
            (@RequestBody SignatureInDTO signatureInDTO) {
        logger.log(INFO, "Put /studentSignatureContract entered with SignatureInDTO: " + signatureInDTO);

        try {
            authService.getToken(signatureInDTO.getToken(), STUDENT);
            StageContractOutDTO dto = studentService.addSignatureToContract(signatureInDTO);
            logger.log(INFO, "Put /studentSignatureContract sent request 200 : " + dto);
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /studentSignatureContract sent request 404");
            return ResponseEntity.notFound().build();
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put /studentSignatureContract sent request 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (InvalidOwnershipException e) {
            logger.log(INFO, "Put /studentSignatureContract sent request 409");
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    @PutMapping("/studentSelectDate")
    public ResponseEntity<InterviewOutDTO> StudentSelectDate(@RequestBody InterviewSelectInDTO interviewDTO){
        logger.log(INFO, "Put /studentSelectDate");

        try {
            authService.getToken(interviewDTO.getToken(), STUDENT);
            InterviewOutDTO dto = studentService.selectInterviewTime(interviewDTO);
            logger.log(INFO, "Put/studentSelectDate return 200");
            return ResponseEntity.ok(dto);
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put /studentSelectDate return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (InvalidTokenException | InvalidOwnershipException e) {
            logger.log(INFO, "Put /studentSelectDate return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (InvalidDateFormatException | InvalidDateException e) {
            logger.log(INFO, "Put /studentSelectDate return 400");
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @PutMapping("/getStudentInterviews/{studentId}")
    public ResponseEntity<List<InterviewOutDTO>> GetStudentInterviews
            (@PathVariable String studentId, @RequestBody TokenDTO token){
        logger.log(INFO, "Put /getStudentInterviews/{studentId}");

        try {
            authService.getToken(token.getToken(), STUDENT);
            List<InterviewOutDTO> interviews = studentService.getInterviews(Long.parseLong(studentId));
            logger.log(INFO, "Put/getStudentInterviews/{studentId} return 200");
            return ResponseEntity.ok(interviews);
        } catch (InvalidTokenException e) {
            logger.log(INFO, "Put/getStudentInterviews/{studentId} return 403");
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (NonExistentEntityException e) {
            logger.log(INFO, "Put/getStudentInterviews/{studentId} return 404");
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }
}
