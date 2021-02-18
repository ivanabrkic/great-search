package great.search.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchRequestBookDTO {
    private String title;
    private String authorFirstName;
    private String authorLastName;

    private List<String> genres;

    private String content;

    private Boolean exclude;
    private Boolean exactMatch;
    private Boolean includeAll;
}
