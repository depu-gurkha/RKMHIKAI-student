package online.rkmhikai.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName="tblChapter",foreignKeys = @ForeignKey(entity = Subject.class,
        parentColumns = "subjectID",childColumns = "subjectId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Chapter implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "chapterID")
    private int chapterNo;
    @ColumnInfo(name = "Description")
    private String desc;
    private String title;
    private int items;
    private int subjectId;

    public Chapter(int chapterNo, String desc, String title, int items, int subjectId) {
        this.chapterNo = chapterNo;
        this.desc = desc;
        this.title = title;
        this.items = items;
        this.subjectId = subjectId;
    }

    public int getChapterNo() {
        return chapterNo;
    }

    public String getDesc() {
        return desc;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getItems() {
        return items;
    }
}

