package great.search.repository;

import great.search.model.Book;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, Long> {

    // EXACT MATCH OR (IF SOME FIELDS ARE EMPTY)

    @Highlight(fields = {
            @HighlightField(name = "title"),
            @HighlightField(name = "authorFirstName"),
            @HighlightField(name = "authorLastName"),
            @HighlightField(name = "genre"),
            @HighlightField(name = "content")
    })
    SearchHits<Book> findBooksByTitleOrAuthorFirstNameOrAuthorLastNameOrGenreIsInOrContent(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

    // EXACT MATCH AND (IF ALL FIELDS ARE PRESENT)

    @Highlight(fields = {
            @HighlightField(name = "title"),
            @HighlightField(name = "authorFirstName"),
            @HighlightField(name = "authorLastName"),
            @HighlightField(name = "genre"),
            @HighlightField(name = "content")
    })
    SearchHits<Book> findBooksByTitleAndAuthorFirstNameAndAuthorLastNameAndGenreIsInAndContent(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

    //  LIKE (IF SOME FIELDS ARE EMPTY)

    @Highlight(fields = {
            @HighlightField(name = "title"),
            @HighlightField(name = "authorFirstName"),
            @HighlightField(name = "authorLastName"),
            @HighlightField(name = "genre"),
            @HighlightField(name = "content")
    })
    SearchHits<Book> findBooksByTitleContainsOrAuthorFirstNameContainsOrAuthorLastNameContainsOrGenreIsInOrContentContains(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

    //  LIKE (IF ALL FIELDS ARE PRESENT)

    @Highlight(fields = {
            @HighlightField(name = "title"),
            @HighlightField(name = "authorFirstName"),
            @HighlightField(name = "authorLastName"),
            @HighlightField(name = "genre"),
            @HighlightField(name = "content")
    })
    SearchHits<Book> findBooksByTitleContainsAndAuthorFirstNameContainsAndAuthorLastNameContainsAndGenreIsInAndContentContains(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

    // NOT (IF ALL FIELDS ARE PRESENT)

    SearchHits<Book> findBooksByTitleNotAndAuthorFirstNameNotAndAuthorLastNameNotAndGenreNotInAndContentNot(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

    // NOT (IF SOME FIELDS ARE EMPTY)

    SearchHits<Book> findBooksByTitleNotOrAuthorFirstNameNotOrAuthorLastNameNotOrGenreNotInOrContentNot(String title, String authorFirstName, String authorLastName, List<String> genre, String content);

}
