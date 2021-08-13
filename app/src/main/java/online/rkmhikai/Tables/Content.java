package online.rkmhikai.Tables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Chapter.class,
        parentColumns = "chapterID",
        childColumns = "chapterID",
        onDelete =ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),tableName = "tblcontent",indices = {@Index("chapterID")})
public class Content {

    private int contentCode;

    @PrimaryKey
    private int contentID;

    private int chapterID;

    private String type;

    private String title;

    private String description;

    private String webUrl;

    private  String localUrl;



    public Content(int contentCode, int contentID, int chapterID, String type, String title, String description, String webUrl,String localUrl) {
        this.contentCode = contentCode;
        this.contentID = contentID;
        this.chapterID = chapterID;
        this.type = type;
        this.title = title;
        this.description = description;
        this.webUrl = webUrl;
        this.localUrl=localUrl;
    }
    @Ignore
    public  Content(String title,String type,int contentID,String webUrl,String localUrl){
        this.title=title;
        this.type=type;
        this.contentID=contentID;
        this.webUrl=webUrl;
        this.localUrl=localUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public int getContentCode() {
        return contentCode;
    }

    public int getContentID() {
        return contentID;
    }

    public int getChapterID() {
        return chapterID;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
