package dwalldorf.snchallenge.worker;

import dwalldorf.snchallenge.exception.UnsupportedTypeException;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.service.DocumentService;
import dwalldorf.snchallenge.service.WatermarkService;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WatermarkWorker {

    private final static Logger logger = LoggerFactory.getLogger(WatermarkWorker.class);

    @Inject
    private WatermarkService watermarkService;

    @Inject
    private DocumentService documentService;

    @Scheduled(fixedRate = 1000)
    public void watermarkDocuments() {
        long startTime = System.nanoTime();
        List<Document> documentsToWatermark = documentService.findLast(100);

        if (documentsToWatermark != null && documentsToWatermark.size() > 0) {
            documentsToWatermark.forEach(document -> {
                try {
                    Document watermarkedDocument = watermarkService.createWatermark(document);
                    documentService.save(watermarkedDocument);

                } catch (UnsupportedTypeException e) {
                    logger.error(e.getMessage());
                }
            });

            long runtimeMs = (System.nanoTime() - startTime) / 1000000;
            logger.info("Watermarked {} documents in {}ms", documentsToWatermark.size(), runtimeMs);
        }
    }
}
