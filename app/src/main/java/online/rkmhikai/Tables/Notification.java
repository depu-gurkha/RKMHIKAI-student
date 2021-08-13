package online.rkmhikai.Tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblNotification")
public class Notification {
    @PrimaryKey
    private int id;
    private String title;
    private String text;
    private String imgNotice;

    public Notification(int id,String title, String text) {
        this.title = title;
        this.text = text;
        this.id=id;

    }
    @Ignore
    public Notification(String title, String text, String imgNotice) {
        this.title = title;
        this.text = text;
        this.imgNotice = imgNotice;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgNotice() { return imgNotice; }

    public void setImgNotice(String imgNotice) { this.imgNotice = imgNotice; }
}
