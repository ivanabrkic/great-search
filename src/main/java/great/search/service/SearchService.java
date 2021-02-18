package great.search.service;

import great.search.dto.SearchRequestBookDTO;
import great.search.dto.SearchRequestReadersDTO;
import great.search.dto.SearchResultBookDTO;
import great.search.dto.SearchResultReadersDTO;
import great.search.model.Book;
import great.search.model.Reader;
import great.search.repository.BookRepository;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    BookRepository bookRepository;

    public List<SearchResultReadersDTO> getBetaReaders(SearchRequestReadersDTO searchRequestReadersDTO) {
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery().mustNot(
                                QueryBuilders
                                        .geoDistanceQuery("location")
                                        .point(searchRequestReadersDTO.getLatitude(), searchRequestReadersDTO.getLongitude())
                                        .distance(searchRequestReadersDTO.getDistance(), DistanceUnit.KILOMETERS)
                        ))
                .build();
        SearchHits<Reader> result = elasticsearchRestTemplate.search(query, Reader.class);
        List<SearchResultReadersDTO> searchResultReadersDTOList = new ArrayList<>();
        for (SearchHit searchHit : result.getSearchHits()) {
            Reader reader = (Reader) searchHit.getContent();
            searchResultReadersDTOList.add(new SearchResultReadersDTO(reader.getId(), reader.getResult()));
        }
        return searchResultReadersDTOList;
    }

    // BOOK SEARCHES

    public List<SearchResultBookDTO> getBooksSearchRepositoryIncludeAll(SearchRequestBookDTO searchRequestBookDTO) {
        if (searchRequestBookDTO.getExclude()){
            System.err.println("ALL = TRUE AND EXCLUDE = TRUE AND EXACT MATCH = FALSE");
            Query query = new NativeSearchQueryBuilder()
                    .withHighlightFields(
                            new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                    )
                    .withQuery(QueryBuilders
                            .boolQuery()
                            .mustNot(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getAuthorLastName() + "*").field("authorLastName")
                            )
                            .mustNot(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getAuthorFirstName() + "*").field("authorFirstName")
                            )
                            .mustNot(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getTitle() + "*").field("title")
                            )
                            .mustNot(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getContent() + "*").field("content")
                            )
                            .mustNot(
                                    QueryBuilders
                                            .termsQuery("genre", searchRequestBookDTO.getGenres())
                            )
                    )
                    .build();
            SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
            return makeResult(result);
        }
        else {
            System.err.println("ALL = TRUE AND EXCLUDE = FALSE AND EXACT MATCH = FALSE");

            Query query = new NativeSearchQueryBuilder()
                    .withHighlightFields(
                            new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                    )
                    .withQuery(QueryBuilders
                            .boolQuery()
                            .must(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getAuthorLastName() + "*").field("authorLastName")
                            )
                            .must(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getAuthorFirstName() + "*").field("authorFirstName")
                            )
                            .must(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getTitle() + "*").field("title")
                            )
                            .must(
                                    QueryBuilders
                                            .queryStringQuery(searchRequestBookDTO.getContent() + "*").field("content")
                            )
                            .must(
                                    QueryBuilders
                                            .termsQuery("genre", searchRequestBookDTO.getGenres())
                            )
                    )
                    .build();
            SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
            return makeResult(result);
        }
    }

    public List<SearchResultBookDTO> getBooksSearchRepositoryNotAll(SearchRequestBookDTO searchRequestBookDTO) {
        System.err.println("ALL = FALSE AND EXACT MATCH = FALSE");
        Query query = new NativeSearchQueryBuilder()
                .withHighlightFields(
                        new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                )
                .withQuery(QueryBuilders
                        .boolQuery()
                        .should(
                                QueryBuilders
                                        .queryStringQuery(searchRequestBookDTO.getAuthorLastName() + "*").field("authorLastName")
                        )
                        .should(
                                QueryBuilders
                                        .queryStringQuery(searchRequestBookDTO.getAuthorFirstName() + "*").field("authorFirstName")
                        )
                        .should(
                                QueryBuilders
                                        .queryStringQuery(searchRequestBookDTO.getTitle() + "*").field("title")
                        )
                        .should(
                                QueryBuilders
                                        .queryStringQuery(searchRequestBookDTO.getContent() + "*").field("content")
                        )
                        .should(
                                QueryBuilders
                                        .termsQuery("genre", searchRequestBookDTO.getGenres())
                        )
                )
                .build();
        SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
        return makeResult(result);
    }

    public List<SearchResultBookDTO> getBooksSearchRepositoryExactMatchIncludeAll(SearchRequestBookDTO searchRequestBookDTO) {
        if (searchRequestBookDTO.getExclude()){
            System.err.println("ALL = TRUE AND EXCLUDE = TRUE AND EXACT MATCH = TRUE");
            Query query = new NativeSearchQueryBuilder()
                    .withHighlightFields(
                            new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                    )
                    .withQuery(QueryBuilders
                            .boolQuery()
                            .mustNot(
                                    QueryBuilders
                                            .matchQuery("authorLastName", searchRequestBookDTO.getAuthorLastName())
                            )
                            .mustNot(
                                    QueryBuilders
                                            .matchQuery("authorFirstName", searchRequestBookDTO.getAuthorFirstName())
                            )
                            .mustNot(
                                    QueryBuilders
                                            .matchQuery("title", searchRequestBookDTO.getTitle())
                            )
                            .mustNot(
                                    QueryBuilders
                                            .matchQuery("content", searchRequestBookDTO.getContent())
                            )
                            .mustNot(
                                    QueryBuilders
                                            .termsQuery("genre", searchRequestBookDTO.getGenres())
                            )
                    )
                    .build();
            SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
            return makeResult(result);
        }
        else {
            System.err.println("ALL = TRUE AND EXCLUDE = FALSE AND EXACT MATCH = TRUE");
            Query query = new NativeSearchQueryBuilder()
                    .withHighlightFields(
                            new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                            new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                    )
                    .withQuery(QueryBuilders
                            .boolQuery()
                            .must(
                                    QueryBuilders
                                            .matchQuery("authorLastName", searchRequestBookDTO.getAuthorLastName())
                            )
                            .must(
                                    QueryBuilders
                                            .matchQuery("authorFirstName", searchRequestBookDTO.getAuthorFirstName())
                            )
                            .must(
                                    QueryBuilders
                                            .matchQuery("title", searchRequestBookDTO.getTitle())
                            )
                            .must(
                                    QueryBuilders
                                            .matchQuery("content", searchRequestBookDTO.getContent())
                            )
                            .must(
                                    QueryBuilders
                                            .termsQuery("genre", searchRequestBookDTO.getGenres())
                            )
                    )
                    .build();
            SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
            return makeResult(result);
        }
    }


    public List<SearchResultBookDTO> getBooksSearchRepositoryExactMatchNotAll(SearchRequestBookDTO searchRequestBookDTO) {
        System.err.println("ALL = FALSE AND EXACT MATCH = TRUE");

        Query query = new NativeSearchQueryBuilder()
                .withHighlightFields(
                        new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                )
                .withQuery(QueryBuilders
                        .boolQuery()
                        .should(
                                QueryBuilders
                                        .matchQuery("authorLastName", searchRequestBookDTO.getAuthorLastName())
                                        .fuzziness(Fuzziness.AUTO)
                                        .prefixLength(3)
                        )
                        .should(
                                QueryBuilders
                                        .matchQuery("authorFirstName", searchRequestBookDTO.getAuthorFirstName())
                                        .fuzziness(Fuzziness.AUTO)
                                        .prefixLength(3)
                        )
                        .should(
                                QueryBuilders
                                        .matchQuery("title", searchRequestBookDTO.getTitle())
                                        .fuzziness(Fuzziness.AUTO)
                                        .prefixLength(3)
                        )
                        .should(
                                QueryBuilders
                                        .matchQuery("content", searchRequestBookDTO.getContent())
                                        .fuzziness(Fuzziness.AUTO)
                                        .prefixLength(3)
                        )
                        .should(
                                QueryBuilders
                                        .termsQuery("genre", searchRequestBookDTO.getGenres())
                        )
                )
                .build();
        SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
        return makeResult(result);
    }

    public List<SearchResultBookDTO> getBooksPhraseSearch(SearchRequestBookDTO searchRequestBookDTO){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withHighlightFields(
                        new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                )
                .withQuery(QueryBuilders
                        .boolQuery()
                        .minimumShouldMatch(1)
                        .should(
                                QueryBuilders.matchPhraseQuery("content", searchRequestBookDTO.getContent()).slop(1)
                        )
                        .should(
                            QueryBuilders.matchPhraseQuery("title", searchRequestBookDTO.getTitle()).slop(1)
                        )
                        .should(
                                QueryBuilders.matchPhraseQuery("authorFirstName", searchRequestBookDTO.getAuthorFirstName()).slop(1)
                        )
                        .should(
                                QueryBuilders.matchPhraseQuery("authorLastName", searchRequestBookDTO.getAuthorLastName()).slop(1)
                        )
                )
                .build();

        SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
        return makeResult(result);
    }

    public List<SearchResultBookDTO> getBooksPhraseSearchExclude(SearchRequestBookDTO searchRequestBookDTO){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withHighlightFields(
                        new HighlightBuilder.Field("content").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("title").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorFirstName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("authorLastName").preTags("<b class='text-warning'>").postTags("</b>"),
                        new HighlightBuilder.Field("genre").preTags("<b class='text-warning'>").postTags("</b>")
                )
                .withQuery(QueryBuilders
                        .boolQuery()
                        .minimumShouldMatch(1)
                        .mustNot(
                                QueryBuilders.matchPhraseQuery("content", searchRequestBookDTO.getContent()).slop(1)
                        )
                        .mustNot(
                                QueryBuilders.matchPhraseQuery("title", searchRequestBookDTO.getTitle()).slop(1)
                        )
                        .mustNot(
                                QueryBuilders.matchPhraseQuery("authorFirstName", searchRequestBookDTO.getAuthorFirstName()).slop(1)
                        )
                        .mustNot(
                                QueryBuilders.matchPhraseQuery("authorLastName", searchRequestBookDTO.getAuthorLastName()).slop(1)
                        )
                )
                .build();

        SearchHits<Book> result = elasticsearchRestTemplate.search(query, Book.class);
        return makeResult(result);
    }

    public List<SearchResultBookDTO> makeResult(SearchHits<Book> searchResult) {
        List<SearchResultBookDTO> searchResultBookDTOS = new ArrayList<>();
        for (SearchHit searchHit : searchResult.getSearchHits()) {
            Book book = (Book) searchHit.getContent();
            String hTitle = "TITLE : ";
            if (searchHit.getHighlightFields().get("title") != null) {
                hTitle +=  searchHit.getHighlightFields().get("title");
            }
            String hGenre = "GENRE : ";
            if (searchHit.getHighlightFields().get("genre") != null) {
                hGenre += searchHit.getHighlightFields().get("genre");
            }
            String hAuthor = "AUTHOR : ";
            if (searchHit.getHighlightFields().get("authorFirstName") != null || searchHit.getHighlightFields().get("authorLastName") != null) {
                if (searchHit.getHighlightFields().get("authorFirstName") != null) {
                   hAuthor +=  searchHit.getHighlightFields().get("authorFirstName") + " ";
                }
                else {
                    hAuthor += book.getAuthorFirstName() + " ";
                }
                if (searchHit.getHighlightFields().get("authorLastName") != null) {
                    hAuthor +=  searchHit.getHighlightFields().get("authorLastName");
                }
                else {
                    hAuthor += book.getAuthorLastName();
                }
            }
            String hContent = "CONTENT : ";
            if (searchHit.getHighlightFields().get("content") != null) {
                hContent += searchHit.getHighlightFields().get("content");
            }
            String result =
                    (searchHit.getHighlightFields().get("title") != null ? "<p>" + hTitle + "</p>" : "<p> TITLE : " + book.getTitle() + "</p>") +
                    (searchHit.getHighlightFields().get("authorLastName") != null || searchHit.getHighlightFields().get("authorLastName") != null ? "<p>" + hAuthor + "</p>" : "<p> AUTHOR : " + book.getAuthorFirstName() + " " + book.getAuthorLastName() + "</p>") +
                    (searchHit.getHighlightFields().get("genre") != null ? "<p>" + hGenre + "</p>" : "<p> GENRE : " + book.getGenre() + "</p>") +
                    (searchHit.getHighlightFields().get("content") != null ? "<p class='text-justify'>" + hContent + "</p>" : "") +
                    book.getResult();
            searchResultBookDTOS.add(new SearchResultBookDTO(book.getId(), result, book.getDownloadLink()));
        }
        return searchResultBookDTOS;
    }
}
