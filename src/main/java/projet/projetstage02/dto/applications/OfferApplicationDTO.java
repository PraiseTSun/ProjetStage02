package projet.projetstage02.dto.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.dto.users.Students.StudentOutDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferApplicationDTO {
    //todo remove this and send the list directly
    List<StudentOutDTO> applicants;
}
