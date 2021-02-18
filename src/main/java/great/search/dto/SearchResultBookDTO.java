package great.search.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResultBookDTO {
    private Long id;
    private String result;
    private String downloadLink;
}
