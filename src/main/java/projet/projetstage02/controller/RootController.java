package projet.projetstage02.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.dto.auth.LoginDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.problems.ProblemInDTO;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.GestionnaireService;

import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.Level.INFO;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static projet.projetstage02.model.Token.UserTypes.*;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class RootController {
    private final AuthService authService;
    private final GestionnaireService gestionnaireService;

    private final Logger logger = LogManager.getLogger(RootController.class);


    private Map<String, String> getError(String error) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("error", error);
        return hashMap;
    }

    @PostMapping("/student/login")
    public ResponseEntity<TokenDTO> studentLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, STUDENT);
            logger.log(INFO, "PostMapping: /student/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PostMapping: /student/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }

    }

    @PostMapping("/gestionnaire/login")
    public ResponseEntity<TokenDTO> gestionnaireLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, GESTIONNAIRE);
            logger.log(INFO, "PostMapping: /gestionnaire/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PostMapping: /gestionnaire/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }

    }

    @PostMapping("/company/login")
    public ResponseEntity<TokenDTO> companyLogin(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.loginIfValid(loginDTO, COMPANY);
            logger.log(INFO, "PostMapping: /company/login sent 201 response");
            return ResponseEntity.status(CREATED).body(TokenDTO.builder().token(token).build());
        } catch (InvalidTokenException e) {
            logger.log(INFO, "PostMapping: /company/login sent 403 response");
            return ResponseEntity.status(FORBIDDEN).build();
        }
    }

    @PostMapping("/reportProblem")
    public ResponseEntity<?> reportProblem(@RequestBody ProblemInDTO problem) {
        gestionnaireService.reportProblem(problem);
        return ResponseEntity.status(201).build();
    }
}