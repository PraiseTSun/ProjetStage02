package projet.projetstage02.DTO;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfDTO {
    private long studentId;
    @NotNull
    private byte[] pdf;
    @NotBlank
    private String token;
}
