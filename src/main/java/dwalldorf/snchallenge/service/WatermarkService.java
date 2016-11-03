package dwalldorf.snchallenge.service;

import dwalldorf.snchallenge.exception.UnsupportedTypeException;
import dwalldorf.snchallenge.model.Book;
import dwalldorf.snchallenge.model.Document;
import dwalldorf.snchallenge.model.Journal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class WatermarkService {

    protected final static String KEY_TITLE = "title";
    protected final static String KEY_TOPIC = "topic";
    protected final static String KEY_AUTHOR = "author";
    protected final static String KEY_CONTENT = "content";

    protected final static String CONTENT_BOOK = "book";
    protected final static String CONTENT_JOURNAL = "journal";

    public Document createWatermark(Document document) throws UnsupportedTypeException {
        if (document instanceof Book) {
            document.setWatermark(createBookWatermark((Book) document));
            return document;
        }
        if (document instanceof Journal) {
            document.setWatermark(createJournalWatermark((Journal) document));
            return document;
        }

        throw new UnsupportedTypeException("Called with unsupported document type: " + document.getClass().getSimpleName());
    }

    private Map<String, String> createBookWatermark(final Book book) {
        Map<String, String> watermark = createBaseWatermark(book);
        watermark.put(KEY_CONTENT, CONTENT_BOOK);
        watermark.put(KEY_TOPIC, book.getTopic().toString());

        return watermark;
    }

    private Map<String, String> createJournalWatermark(final Journal journal) {
        Map<String, String> watermark = createBaseWatermark(journal);
        watermark.put(KEY_CONTENT, CONTENT_JOURNAL);

        return watermark;
    }

    private Map<String, String> createBaseWatermark(final Document document) {
        Map<String, String> watermark = new HashMap<>();
        watermark.put(KEY_TITLE, document.getTitle());
        watermark.put(KEY_AUTHOR, document.getAuthor());

        return watermark;
    }
}
