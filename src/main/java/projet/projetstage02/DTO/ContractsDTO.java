package projet.projetstage02.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ContractsDTO {
    private List<StageContractOutDTO> contracts;

    public ContractsDTO() {
        contracts = new ArrayList<>();
    }

    public void add(StageContractOutDTO stageContractOutDTO) {
        contracts.add(stageContractOutDTO);
    }

    public int size() {
        return contracts.size();
    }
}
