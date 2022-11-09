package projet.projetstage02.dto.pdf;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfDTO {
    private long studentId;
    @NotNull
    @ToString.Exclude
    private byte[] pdf;
    @NotBlank
    private String token;
}