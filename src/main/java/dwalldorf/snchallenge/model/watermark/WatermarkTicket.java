package dwalldorf.snchallenge.model.watermark;

import java.io.Serializable;

public class WatermarkTicket implements Serializable {

    public WatermarkTicket() {
    }

    public WatermarkTicket(String ticketId) {
        this.ticketId = ticketId;
    }

    private String ticketId;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
