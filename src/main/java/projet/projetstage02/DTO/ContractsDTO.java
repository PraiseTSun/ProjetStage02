package projet.projetstage02.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContractsDTO {
    private List<StageContractOutDTO> applications;

    public ContractsDTO() {
        applications = new ArrayList<>();
    }

    public void add(StageContractOutDTO stageContractOutDTO) {
        applications.add(stageContractOutDTO);
    }

    public int size() {
        return applications.size();
    }
}
