package projet.projetstage02.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class RootController {

    @PostMapping("/createUser")
    public ResponseEntity<Boolean> getUsers(){

        return true ?
                ResponseEntity.status(HttpStatus.CREATED).body(true)
                : ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
}
