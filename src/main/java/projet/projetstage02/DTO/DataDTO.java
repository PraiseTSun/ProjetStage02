package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DataDTO<T> {
    private T data;
}
