package dwalldorf.snchallenge.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class Book extends Document {

    public enum BOOK_TOPIC {
        BUSINESS, SCIENCE, MEDIA
    }

    private BOOK_TOPIC topic;

    public BOOK_TOPIC getTopic() {
        return topic;
    }

    public Book setTopic(BOOK_TOPIC topic) {
        this.topic = topic;
        return this;
    }
}
