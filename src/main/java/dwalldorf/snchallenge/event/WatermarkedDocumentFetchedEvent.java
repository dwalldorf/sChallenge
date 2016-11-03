package dwalldorf.snchallenge.event;

import dwalldorf.snchallenge.model.Document;

public class WatermarkedDocumentFetchedEvent {

    private Document document;

    public WatermarkedDocumentFetchedEvent(final Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
