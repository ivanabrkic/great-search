package great.search.controller;

import great.search.dto.SearchRequestBookDTO;
import great.search.dto.SearchRequestReadersDTO;
import great.search.dto.SearchResultBookDTO;
import great.search.dto.SearchResultReadersDTO;
import great.search.model.Book;
import great.search.model.Reader;
import great.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping(value = "/readers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<SearchResultReadersDTO>> getBetaReaders(@RequestBody SearchRequestReadersDTO searchRequestReadersDTO){
        List<SearchResultReadersDTO> searchHits = searchService.getBetaReaders(searchRequestReadersDTO);
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }

    // BOOKS SEARCH

    @PostMapping(value = "/book/repo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<SearchResultBookDTO>> searchBookRepository(@RequestBody SearchRequestBookDTO searchRequestBookDTO){
        List<SearchResultBookDTO> searchHits;
        if (searchRequestBookDTO.getExactMatch()) {
            if (searchRequestBookDTO.getIncludeAll()) {
                searchHits = searchService.getBooksSearchRepositoryExactMatchIncludeAll(searchRequestBookDTO);
            }
            else {
                searchHits = searchService.getBooksSearchRepositoryExactMatchNotAll(searchRequestBookDTO);
            }
        }
        else {
            if (searchRequestBookDTO.getIncludeAll()) {
                searchHits = searchService.getBooksSearchRepositoryIncludeAll(searchRequestBookDTO);
            }
            else {
                searchHits = searchService.getBooksSearchRepositoryNotAll(searchRequestBookDTO);
            }
        }
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }

    @PostMapping(value = "/book/phrase", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<SearchResultBookDTO>> searchBookPhrase(@RequestBody SearchRequestBookDTO searchRequestBookDTO){
        List<SearchResultBookDTO> searchHits = searchService.getBooksPhraseSearch(searchRequestBookDTO);
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }

    @PostMapping(value = "/book/phrase/exclude", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<SearchResultBookDTO>> searchBookPhraseExclude(@RequestBody SearchRequestBookDTO searchRequestBookDTO){
        List<SearchResultBookDTO> searchHits = searchService.getBooksPhraseSearchExclude(searchRequestBookDTO);
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }
}
