package projet.projetstage02.dto.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveApplicationDTO {
    private String token;
    private long applicationId;
    private long studentId;
}
