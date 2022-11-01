package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
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
