package online.rkmhikai.Tables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Content.class,
        parentColumns = "contentID",
        childColumns = "contentID",
        onDelete =ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),tableName = "tblquestion",indices = {@Index("contentID")})
public class Question {

    @PrimaryKey
    private int questionID;

    private int contentID;

    private String question;

    public Question(int questionID, int contentID, String question) {
        this.questionID = questionID;
        this.contentID = contentID;
        this.question = question;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public int getContentID() {
        return contentID;
    }

    public String getQuestion() {
        return question;
    }
}
