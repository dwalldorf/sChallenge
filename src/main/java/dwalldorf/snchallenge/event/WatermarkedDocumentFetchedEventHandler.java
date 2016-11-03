package dwalldorf.snchallenge.event;

import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.service.DocumentService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WatermarkedDocumentFetchedEventHandler {

    private final static Logger logger = LoggerFactory.getLogger(WatermarkedDocumentFetchedEventHandler.class);

    @Inject
    private DocumentService documentService;

    @Async
    @EventListener
    public void onWatermarkedDocumentFetchedEvent(final WatermarkedDocumentFetchedEvent event) {
        Document document = event.getDocument();
        documentService.delete(document);

        logger.info(
                "Watermarked document with ticket id {} has been fetched and deleted",
                document.getWatermarkTicketId()
        );
    }
}
