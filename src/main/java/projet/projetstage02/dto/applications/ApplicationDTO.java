package projet.projetstage02.dto.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    public long studentId;
    public String fullName;
    public long offerId;
    public String company;
}
