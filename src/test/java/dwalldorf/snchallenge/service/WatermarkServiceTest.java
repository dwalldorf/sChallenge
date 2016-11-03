package dwalldorf.snchallenge.service;

import static dwalldorf.snchallenge.service.WatermarkService.KEY_AUTHOR;
import static dwalldorf.snchallenge.service.WatermarkService.CONTENT_BOOK;
import static dwalldorf.snchallenge.service.WatermarkService.CONTENT_JOURNAL;
import static dwalldorf.snchallenge.service.WatermarkService.KEY_CONTENT;
import static dwalldorf.snchallenge.service.WatermarkService.KEY_TITLE;
import static dwalldorf.snchallenge.service.WatermarkService.KEY_TOPIC;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import dwalldorf.snchallenge.exception.UnsupportedTypeException;
import dwalldorf.snchallenge.model.Book;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.Journal;
import dwalldorf.snchallenge.model.UnsupportedDocumentType;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class WatermarkServiceTest {

    private final String title = "book";
    private final String author = "Max Mustermann";
    private final Book.BOOK_TOPIC topic = Book.BOOK_TOPIC.MEDIA;

    @InjectMocks
    private WatermarkService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UnsupportedTypeException.class)
    public void testCreateWatermarkWithUnsupportedType() throws Exception {
        UnsupportedDocumentType document = new UnsupportedDocumentType();
        service.createWatermark(document);
    }

    @Test
    public void testCreateWatermarkSetsBookContent() throws Exception {
        Book book = createBook();

        Document watermarkedDocument = service.createWatermark(book);
        assertEquals(CONTENT_BOOK, watermarkedDocument.getWatermark().get(KEY_CONTENT));
    }

    @Test
    public void testCreateWatermarkSetsJournalContent() throws Exception {
        Journal journal = createJournal();

        Document watermarkedDocument = service.createWatermark(journal);
        assertEquals(CONTENT_JOURNAL, watermarkedDocument.getWatermark().get(KEY_CONTENT));
    }

    @Test
    public void testCreateWatermarkBook() throws Exception {
        Book book = createBook();

        Document watermarkedDocument = service.createWatermark(book);
        Map<String, String> watermark = watermarkedDocument.getWatermark();

        assertNotNull(watermark);

        assertEquals(CONTENT_BOOK, watermark.get(KEY_CONTENT));
        assertEquals(title, watermark.get(KEY_TITLE));
        assertEquals(author, watermark.get(KEY_AUTHOR));
        assertEquals(topic.toString(), watermark.get(KEY_TOPIC));
    }

    @Test
    public void testCreateWatermarkJournal() throws Exception {
        Journal journal = createJournal();

        Document watermarkedDocument = service.createWatermark(journal);
        Map<String, String> watermark = watermarkedDocument.getWatermark();

        assertNotNull(watermark);

        assertEquals(CONTENT_JOURNAL, watermark.get(KEY_CONTENT));
        assertEquals(title, watermark.get(KEY_TITLE));
        assertEquals(author, watermark.get(KEY_AUTHOR));
        assertNull(watermark.get(KEY_TOPIC));
    }

    private Book createBook() {
        Book book = new Book();
        book.setTopic(topic)
            .setTitle(title)
            .setAuthor(author);

        return book;
    }

    private Journal createJournal() {
        Journal journal = new Journal();
        journal.setTitle(title)
               .setAuthor(author);

        return journal;
    }
}
