package projet.projetstage02.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentNotificationDTO {
    private long nbUploadCv;
    private long nbStages;
    private long nbContracts;
}
