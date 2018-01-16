package si.uni_lj.fri.rso.ir_messaging.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;

@Entity(name = "messages")
@NamedQueries(value = {
        @NamedQuery(name = "Message.getAll", query = "SELECT p FROM messages p"),
        @NamedQuery(name = "Message.getFrom", query = "SELECT p FROM messages p WHERE p.userFrom LIKE :idFrom"),
        @NamedQuery(name = "Message.getTo", query = "SELECT p FROM messages p WHERE p.userTo LIKE :idTo")
})
@UuidGenerator(name = "idGenerator")
public class Message {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "user_from")
    private String userFrom;

    @Column(name = "user_to")
    private String userTo;

    private String date;

    private String time;

    @Column(columnDefinition = "VARCHAR")
    private String message;

    public Message(String id, String userFrom, String userTo, String date, String time, String message) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public Message() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String date) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
