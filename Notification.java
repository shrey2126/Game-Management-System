import java.security.Timestamp;

class Notification {
    int id;
    String user_name;
    String message;
    Timestamp sentDate;

    Notification(int id, String user_name, String message, Timestamp sentDate) {
        this.id = id;
        this.user_name = user_name;
        this.message = message;
        this.sentDate = sentDate;
    }
}