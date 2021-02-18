package great.search.controller;

import great.search.dto.BookDTO;
import great.search.dto.ReaderDTO;

import great.search.service.IndexingBookService;
import great.search.service.IndexingReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("index")
public class IndexingController {

    @Autowired
    private IndexingReaderService indexingReaderService;

    @Autowired
    private IndexingBookService indexingBookService;

    @PostMapping(value = "/reader", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> save(@RequestBody ReaderDTO readerDTO){
        String status = indexingReaderService.save(readerDTO);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping(value = "/reader/{id}")
    private ResponseEntity<String> delete(@PathVariable Long id){
        String status = indexingReaderService.delete(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> saveBook(@RequestBody BookDTO bookDTO){
        String status = indexingBookService.save(bookDTO);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping(value = "/book/{id}")
    private ResponseEntity<String> deleteBook(@PathVariable Long id){
        String status = indexingBookService.delete(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
