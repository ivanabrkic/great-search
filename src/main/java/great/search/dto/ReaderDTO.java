package great.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReaderDTO {
    private Long id;
    private String result;
    private double longitude;
    private double latitude;
}
