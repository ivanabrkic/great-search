package great.search.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookDTO {
    private Long id;
    private String title;
    private String genre;
    private String content;
    private String authorFirstName;
    private String authorLastName;
    private String downloadLink;
    private String result;
}
