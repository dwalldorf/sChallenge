package dwalldorf.snchallenge.repository;

import dwalldorf.snchallenge.model.Document;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Document findByWatermarkTicketId(String ticketId);

    List<Document> findByWatermarkStringIsNullOrderByIdDesc(Pageable pageable);
}
