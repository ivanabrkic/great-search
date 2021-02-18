package great.search.repository;

import great.search.model.Reader;
import org.elasticsearch.common.geo.GeoDistance;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ReaderRepository extends ElasticsearchRepository<Reader, Long> {

    List<Reader> findReaderByLocationIsNear(GeoPoint location);

}
