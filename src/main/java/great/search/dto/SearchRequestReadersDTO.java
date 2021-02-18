package great.search.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchRequestReadersDTO {

    double latitude;
    double longitude;
    double distance;
}
