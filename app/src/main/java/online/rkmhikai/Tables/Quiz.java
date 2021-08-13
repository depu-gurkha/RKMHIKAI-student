package online.rkmhikai.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="tblQuiz",foreignKeys = @ForeignKey(entity = Content.class,
        parentColumns = "contentID",childColumns = "ContentID",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Quiz {
    @PrimaryKey
    @ColumnInfo(name = "QuestionId")
    private int id;
    @ColumnInfo(name="ContentID")
    public int qContentId;
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    @ColumnInfo(name="CorrectAnswer")
    int correctAns;
    public Quiz(int id, int qContentId, String question, String option1, String option2, String option3, String option4, int correctAns) {
        this.id = id;
        this.qContentId = qContentId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAns = correctAns;
    }

    public int getId() {
        return id;
    }

    public int getqContentId() {
        return qContentId;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getCorrectAns() {
        return correctAns;
    }
}
