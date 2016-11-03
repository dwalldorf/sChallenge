package dwalldorf.snchallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
public abstract class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String watermarkTicketId;

    @Transient
    private Map<String, String> watermark;

    @Column
    @Access(AccessType.PROPERTY)
    @JsonIgnore
    private String watermarkString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Document setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Document setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getWatermarkTicketId() {
        return watermarkTicketId;
    }

    public Document setWatermarkTicketId(String watermarkTicketId) {
        this.watermarkTicketId = watermarkTicketId;
        return this;
    }

    public Map<String, String> getWatermark() {
        return watermark;
    }

    public Document setWatermark(Map<String, String> watermark) {
        this.watermark = watermark;

        if (watermark != null) {
            this.watermarkString =
                    watermark.entrySet()
                             .stream()
                             .map(entry -> entry.getKey() + ":" + entry.getValue())
                             .collect(Collectors.joining(";"));
        }
        return this;
    }

    public String getWatermarkString() {
        return watermarkString;
    }

    public void setWatermarkString(String watermarkString) {
        this.watermarkString = watermarkString;

        if (watermarkString != null && watermarkString.contains(":")) {
            this.watermark =
                    Arrays.asList(watermarkString.split(";"))
                          .stream()
                          .map(elem -> elem.split(":"))
                          .collect(Collectors.toMap(e -> e[0], e -> e[1]));
        }
    }
}
