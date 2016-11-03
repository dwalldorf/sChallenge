package dwalldorf.snchallenge.rest.controller;

import dwalldorf.snchallenge.exception.NotFoundException;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.watermark.WatermarkTicket;
import dwalldorf.snchallenge.service.DocumentService;
import dwalldorf.snchallenge.service.WatermarkScheduleService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Inject
    private WatermarkScheduleService watermarkScheduleService;

    @Inject
    private DocumentService documentService;

    @RequestMapping("/{ticketId}")
    public Document getWatermarkedDocument(@PathVariable String ticketId) throws NotFoundException {
        return watermarkScheduleService.getWatermarkedDocument(new WatermarkTicket(ticketId));
    }

    @RequestMapping
    public List<Document> getAll() {
        return documentService.findAll();
    }
}
