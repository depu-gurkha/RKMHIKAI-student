package online.rkmhikai.Tables;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblsubject")
public class Subject {

    @PrimaryKey
    private int subjectID;

    private String title;

    private String description;

    private int status;

    //For Recycler View
    @Ignore
    public Subject(int subjectID, String title) {
        this.subjectID = subjectID;
        this.title = title;
    }

    public Subject(int subjectID, String title, String description, int status) {
        this.subjectID = subjectID;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public void setSubjectID(int id) {
        this.subjectID = id;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }
}
