package projet.projetstage02.dto.cv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.CvStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvStatusDTO {

    @NotBlank
    @Pattern(regexp = ("NOTHING|PENDING|ACCEPTED|REFUSED"))
    String status;
    String refusalMessage;

    public CvStatusDTO(CvStatus cvStatus) {
        this.status = cvStatus.getStatus();
        this.refusalMessage = cvStatus.getRefusalMessage();
    }

    public CvStatus toModel(CvStatusDTO cvStatusDTO) {
        return CvStatus.builder()
                .refusalMessage(cvStatusDTO.getRefusalMessage())
                .status(cvStatusDTO.getStatus())
                .build();
    }
}
