package projet.projetstage02.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UnvalidatedAcceptationsDTO {
    private List<UnvalidatedAcceptationDTO> applications;

    public UnvalidatedAcceptationsDTO(){
        applications = new ArrayList<>();
    }

    public void add(UnvalidatedAcceptationDTO unvalidatedAcceptationDTO){
        applications.add(unvalidatedAcceptationDTO);
    }

    public int size(){
        return applications.size();
    }
}
