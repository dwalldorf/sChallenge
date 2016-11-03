package dwalldorf.snchallenge.service;

import static dwalldorf.snchallenge.service.WatermarkService.KEY_AUTHOR;
import static dwalldorf.snchallenge.service.WatermarkService.KEY_TITLE;
import static dwalldorf.snchallenge.service.WatermarkService.KEY_TOPIC;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import dwalldorf.snchallenge.exception.NotFoundException;
import dwalldorf.snchallenge.helper.RandomHelper;
import dwalldorf.snchallenge.model.Book;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.Journal;
import dwalldorf.snchallenge.model.watermark.WatermarkTicket;
import dwalldorf.snchallenge.worker.WatermarkWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class WatermarkScheduleServiceBlackBoxIT {

    @Inject
    private WatermarkScheduleService scheduleService;

    @Inject
    private DocumentService documentService;

    @Inject
    private WatermarkWorker watermarkWorker;

    @Test
    public void testScheduleBookWaitForWatermarkGetDocument() throws Exception {
        int booksToCreate = 100;
        List<WatermarkTicket> tickets = new ArrayList<>();


        for (int i = 0; i < booksToCreate; i++) {
            tickets.add(scheduleService.scheduleDocument(createBook()));
        }
        watermarkWorker.watermarkDocuments();

        tickets.forEach(ticket -> {
            Book watermarkedDocument = null;
            try {
                watermarkedDocument = (Book) scheduleService.getWatermarkedDocument(ticket);
                Map<String, String> watermark = watermarkedDocument.getWatermark();

                assertNotNull(watermark);
                assertEquals(watermarkedDocument.getTitle(), watermark.get(KEY_TITLE));
                assertEquals(watermarkedDocument.getAuthor(), watermark.get(KEY_AUTHOR));
                assertEquals(watermarkedDocument.getTopic().toString(), watermark.get(KEY_TOPIC).toString());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        List<Document> all = documentService.findAll();
        assertEquals(0, all.size());
    }

    @Test
    public void testScheduleJournalWaitForWatermarkGetDocument() throws Exception {
        int journalsToCreate = 100;
        List<WatermarkTicket> tickets = new ArrayList<>();


        for (int i = 0; i < journalsToCreate; i++) {
            tickets.add(scheduleService.scheduleDocument(createJournal()));
        }
        watermarkWorker.watermarkDocuments();

        tickets.forEach(ticket -> {
            Journal watermarkedDocument = null;
            try {
                watermarkedDocument = (Journal) scheduleService.getWatermarkedDocument(ticket);
                Map<String, String> watermark = watermarkedDocument.getWatermark();

                assertNotNull(watermark);
                assertEquals(watermarkedDocument.getTitle(), watermark.get(KEY_TITLE));
                assertEquals(watermarkedDocument.getAuthor(), watermark.get(KEY_AUTHOR));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        List<Document> all = documentService.findAll();
        assertEquals(0, all.size());
    }

    private Document createBook() {
        Book book = new Book();
        book.setTopic(Book.BOOK_TOPIC.BUSINESS)
            .setAuthor(RandomHelper.randomString(10))
            .setTitle(RandomHelper.randomString(10));
        return book;
    }

    private Document createJournal() {
        Journal journal = new Journal();
        journal.setAuthor(RandomHelper.randomString(10))
               .setTitle(RandomHelper.randomString(10));
        return journal;
    }
}
