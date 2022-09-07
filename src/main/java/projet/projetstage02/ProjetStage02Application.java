package projet.projetstage02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.projetstage02.service.StudentService;

@SpringBootApplication
public class ProjetStage02Application implements CommandLineRunner {

    @Autowired
    private StudentService studentService;
    public static void main(String[] args) {
        SpringApplication.run(ProjetStage02Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
