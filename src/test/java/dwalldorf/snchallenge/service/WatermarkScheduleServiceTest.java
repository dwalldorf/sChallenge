package dwalldorf.snchallenge.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import dwalldorf.snchallenge.exception.NotFoundException;
import dwalldorf.snchallenge.model.Book;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.Journal;
import dwalldorf.snchallenge.model.watermark.WatermarkTicket;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class WatermarkScheduleServiceTest {

    @InjectMocks
    private WatermarkScheduleService service;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private DocumentService documentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testScheduleDocument() throws Exception {
        Document document = createBook();
        Document persistedReturnDocument = createBook();
        persistedReturnDocument.setId(1);

        when(documentService.save(any(Document.class))).thenReturn(persistedReturnDocument);
        WatermarkTicket ticket = service.scheduleDocument(document);

        assertNotNull(ticket);
        assertEquals(persistedReturnDocument.getWatermarkTicketId(), ticket.getTicketId());
    }

    @Test(expected = NotFoundException.class)
    public void testIsWatermarkingFinishedNotExistingTicket() throws Exception {
        final String ticketId = "1";
        WatermarkTicket ticket = new WatermarkTicket(ticketId);

        when(documentService.findByWatermarkTicketId(eq(ticketId))).thenReturn(null);
        service.isWatermarkingFinished(ticket);
    }

    @Test
    public void testIsWatermarkingFinishedWhenNot() throws Exception {
        final String ticketId = "1";
        WatermarkTicket ticket = new WatermarkTicket(ticketId);
        Document mockPersistedDocument = createBook();

        when(documentService.findByWatermarkTicketId(eq(ticketId))).thenReturn(mockPersistedDocument);
        boolean watermarkingFinished = service.isWatermarkingFinished(ticket);

        assertFalse(watermarkingFinished);
    }

    @Test
    public void testIsWatermarkingFinished() throws Exception {
        final String ticketId = "2";
        WatermarkTicket ticket = new WatermarkTicket(ticketId);
        Document mockPersistedDocument = createBook();
        Map<String, String> mockWatermark = new HashMap<>();
        mockPersistedDocument.setWatermark(mockWatermark);

        when(documentService.findByWatermarkTicketId(eq(ticketId))).thenReturn(mockPersistedDocument);
        boolean watermarkingFinished = service.isWatermarkingFinished(ticket);

        assertTrue(watermarkingFinished);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWatermarkedDocumentNotExistingTicket() throws Exception {
        when(documentService.findByWatermarkTicketId(any())).thenReturn(null);
        service.getWatermarkedDocument(new WatermarkTicket("1"));
    }

    @Test
    public void testGetWatermarkedDocumentNotWatermarked() throws Exception {
        final String ticketId = "2";
        WatermarkTicket ticket = new WatermarkTicket(ticketId);
        Document mockPersistedDocument = createBook();

        when(documentService.findByWatermarkTicketId(eq(ticketId))).thenReturn(mockPersistedDocument);

        Document watermarkedDocument = service.getWatermarkedDocument(ticket);
        assertNull(watermarkedDocument);
    }

    @Test
    public void testGetWatermarkedDocument() throws Exception {
        final String ticketId = "2";
        WatermarkTicket ticket = new WatermarkTicket(ticketId);
        Document mockPersistedDocument = createBook();
        Map<String, String> mockWatermark = new HashMap<>();
        mockPersistedDocument.setWatermark(mockWatermark);

        when(documentService.findByWatermarkTicketId(eq(ticketId))).thenReturn(mockPersistedDocument);

        Document watermarkedDocument = service.getWatermarkedDocument(ticket);
        assertNotNull(watermarkedDocument);
    }

    private Document createBook() {
        Book book = new Book();
        book.setTopic(Book.BOOK_TOPIC.BUSINESS)
            .setAuthor("Max Mustermann")
            .setTitle("Business Magazine");
        return book;
    }

    private Document createJournal() {
        Journal journal = new Journal();
        journal.setTitle("Some Journal")
               .setAuthor("John Doe");
        return journal;
    }
}