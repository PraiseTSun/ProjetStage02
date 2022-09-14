package projet.projetstage02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

@RestController
@RequestMapping("/")
public class RootController {
    @Autowired
    StudentService studentService;
    @Autowired
    CompanyService companyService;
    @Autowired
    GestionnaireService gestionnaireService;


    private ResponseEntity<String> createStudent(StudentDTO studentDTO){
        if(!studentService.isUniqueEmail(studentDTO.getEmail())){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity<String> createCompany(CompanyDTO companyDTO){
        if(!companyService.isUniqueEmail(companyDTO.getEmail())){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    private ResponseEntity<String> createGestionnaire(GestionnaireDTO gestionnaireDTO){
        if(!gestionnaireService.isUniqueEmail(gestionnaireDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cette adresse email est déjà utilisée.");
        }
        gestionnaireService.createGestionnaire(gestionnaireDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUsers(@RequestBody AbstractUserDTO<?> userDTO){
        if(userDTO instanceof GestionnaireDTO){
            return createGestionnaire((GestionnaireDTO) userDTO);
        }
        else if(userDTO instanceof CompanyDTO){
            return  createCompany((CompanyDTO) userDTO);
        }
        else{
            return createStudent((StudentDTO) userDTO);
        }
    }
}
