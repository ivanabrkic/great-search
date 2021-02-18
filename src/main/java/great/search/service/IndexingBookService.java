package great.search.service;

import great.search.dto.BookDTO;
import great.search.dto.ReaderDTO;
import great.search.model.Book;
import great.search.model.Reader;
import great.search.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

@Service
public class IndexingBookService {

    @Autowired
    private BookRepository bookRepository;

    public String save(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setContent(bookDTO.getContent());
        book.setResult(bookDTO.getResult());
        book.setTitle(bookDTO.getTitle());
        book.setDownloadLink(bookDTO.getDownloadLink());
        book.setAuthorFirstName(bookDTO.getAuthorFirstName());
        book.setAuthorLastName(bookDTO.getAuthorLastName());
        book.setGenre(bookDTO.getGenre());
        book = bookRepository.save(book);
        if (book != null) {
            return "Success";
        }
        else {
            return "Failed";
        }
    }

    public String update(Long id) {
        return "";
    }

    public String delete(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
            return "Success";
        }
        else {
            return "Failed";
        }
    }
}
