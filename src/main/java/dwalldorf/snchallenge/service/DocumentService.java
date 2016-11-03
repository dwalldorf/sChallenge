package dwalldorf.snchallenge.service;

import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.repository.DocumentRepository;
import java.util.List;
import javax.inject.Inject;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Inject
    private DocumentRepository documentRepository;

    public Document findByWatermarkTicketId(final String ticketId) {
        return documentRepository.findByWatermarkTicketId(ticketId);
    }

    public List<Document> findLast(final int amount) {
        return documentRepository.findByWatermarkStringIsNullOrderByIdDesc(new PageRequest(0, amount));
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public void delete(Document document) {
        documentRepository.delete(document.getId());
    }
}
