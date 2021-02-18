package great.search.service;

import great.search.dto.ReaderDTO;
import great.search.model.Reader;
import great.search.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

@Service
public class IndexingReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    public String save(ReaderDTO readerDTO) {
        Reader reader = new Reader(readerDTO.getId(), readerDTO.getResult(), new GeoPoint(readerDTO.getLatitude(), readerDTO.getLongitude()));
        reader = readerRepository.save(reader);
        if (reader != null) {
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
        if (readerRepository.findById(id).isPresent()) {
            readerRepository.deleteById(id);
            return "Success";
        }
        else {
            return "Failed";
        }
    }
}
