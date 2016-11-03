package dwalldorf.snchallenge.service;

import dwalldorf.snchallenge.event.WatermarkedDocumentFetchedEvent;
import dwalldorf.snchallenge.exception.NotFoundException;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.watermark.WatermarkTicket;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class WatermarkScheduleService {

    @Inject
    private ApplicationEventPublisher eventPublisher;

    @Inject
    private DocumentService documentService;

    @Transactional
    public WatermarkTicket scheduleDocument(Document document) {
        Document savedDocument = documentService.save(document);

        // in real life push to queue and let workers poll it; maybe use queue id as ticket id
        savedDocument.setWatermarkTicketId(String.valueOf(savedDocument.getId()));
        documentService.save(savedDocument);

        return new WatermarkTicket(savedDocument.getWatermarkTicketId());
    }

    public boolean isWatermarkingFinished(final WatermarkTicket ticket) throws NotFoundException {
        Document document = documentService.findByWatermarkTicketId(ticket.getTicketId());

        if (document == null) {
            throw new NotFoundException();
        }
        return isWatermarkingFinished(document);
    }

    public Document getWatermarkedDocument(final WatermarkTicket ticket) throws NotFoundException {
        Document document = documentService.findByWatermarkTicketId(ticket.getTicketId());
        if (document == null) {
            throw new NotFoundException();
        }

        if (isWatermarkingFinished(document)) {
            eventPublisher.publishEvent(new WatermarkedDocumentFetchedEvent(document));
            return document;
        }
        return null;
    }

    private boolean isWatermarkingFinished(final Document document) {
        return document.getWatermark() != null;
    }
}
