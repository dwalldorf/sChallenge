package dwalldorf.snchallenge.rest.controller;

import dwalldorf.snchallenge.exception.NotFoundException;
import dwalldorf.snchallenge.model.Book;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.Journal;
import dwalldorf.snchallenge.model.watermark.WatermarkTicket;
import dwalldorf.snchallenge.service.WatermarkScheduleService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/schedule")
public class DocumentScheduleController {

    @Inject
    private WatermarkScheduleService scheduleService;

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<WatermarkTicket> scheduleBook(@RequestBody Book book) {
        return new ResponseEntity<>(scheduleDocument(book), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public ResponseEntity<WatermarkTicket> scheduleJournal(@RequestBody Journal journal) {
        return new ResponseEntity<>(scheduleDocument(journal), HttpStatus.CREATED);
    }

    @RequestMapping("/status/{ticketId}")
    public Boolean isWatermarkingFinished(@PathVariable String ticketId) throws NotFoundException {
        return scheduleService.isWatermarkingFinished(new WatermarkTicket(ticketId));
    }

    private WatermarkTicket scheduleDocument(Document document) {
        return scheduleService.scheduleDocument(document);
    }
}
