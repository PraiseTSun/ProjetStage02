package projet.projetstage02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

import java.time.LocalDateTime;
import java.sql.Timestamp;

@RestController
@RequestMapping("/")
public class RootController {
    @Autowired
    StudentService studentService;
    @Autowired
    CompanyService companyService;
    @Autowired
    GestionnaireService gestionnaireService;

    private final long DAY = 864000000;

    @PostMapping("/createStudent")
    public ResponseEntity<String> createStudent(@RequestBody StudentDTO studentDTO){
        if(!studentService.isUniqueEmail(studentDTO.getEmail())){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        studentService.createStudent(studentDTO);
        studentService.sendConfirmationMail(studentDTO.getOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/createCompany")
    public ResponseEntity<String> createCompany(@RequestBody CompanyDTO companyDTO){
        if(!companyService.isUniqueEmail(companyDTO.getEmail())){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        companyService.createCompany(companyDTO);
        companyService.sendConfirmationMail(companyDTO.getOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/createGestionaire")
    public ResponseEntity<String> createGestionnaire(@RequestBody GestionnaireDTO gestionnaireDTO){
        if(!gestionnaireService.isUniqueEmail(gestionnaireDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        gestionnaireService.createGestionnaire(gestionnaireDTO);
        gestionnaireService.sendConfirmationMail(gestionnaireDTO.getOrigin());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private long now(){
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }
    @PutMapping("/confirmEmail/student/{id}")
    public ResponseEntity<String> confirmStudentMail(@PathVariable String id){
        StudentDTO studentDTO = studentService.getUserById(Long.parseLong(id));
        if(studentDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(now() - studentDTO.getInscriptionTimestamp() > DAY){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("La période de confirmation est expirée");
        }
        studentDTO.setEmailConfirmed(true);
        studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/confirmEmail/company/{id}")
    public ResponseEntity<String> confirmCompanyMail(@PathVariable String id){
        CompanyDTO companyDTO = companyService.getUserById(Long.parseLong(id));
        if(companyDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(now() - companyDTO.getInscriptionTimestamp() > DAY){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("La période de confirmation est expirée");
        }
        companyDTO.setEmailConfirmed(true);
        companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/confirmEmail/gestionnaire/{id}")
    public ResponseEntity<String> confirmGestionnaireMail(@PathVariable String id){
        GestionnaireDTO gestionnaireDTO = gestionnaireService.getUserById(Long.parseLong(id));
        if(gestionnaireDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(now() - gestionnaireDTO.getInscriptionTimestamp() > DAY){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("La période de confirmation est expirée");
        }
        gestionnaireDTO.setEmailConfirmed(true);
        gestionnaireService.createGestionnaire(gestionnaireDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
