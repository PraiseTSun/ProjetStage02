package projet.projetstage02.DTO;

import java.util.ArrayList;
import java.util.List;

public class UnvalidatedAcceptationsDTO {
    private List<UnvalidatedAcceptationDTO> applications;

    public UnvalidatedAcceptationsDTO(){
        applications = new ArrayList<>();
    }

    public void add(UnvalidatedAcceptationDTO unvalidatedAcceptationDTO){
        applications.add(unvalidatedAcceptationDTO);
    }
}
