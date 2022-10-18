package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostulOutDTO {
    public long studentId;
    public String fullName;
    public long offerId;
    public String company;
}
