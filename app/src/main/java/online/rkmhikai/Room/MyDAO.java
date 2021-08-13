package online.rkmhikai.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import online.rkmhikai.Tables.Chapter;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.Tables.Notification;
import online.rkmhikai.Tables.Question;
import online.rkmhikai.Tables.Quiz;
import online.rkmhikai.Tables.Subject;


@Dao
public interface  MyDAO {
    @Insert
    void insertSubject(Subject subject);

    @Insert
    void insertChapter(Chapter chapter);

    @Insert
    void insertContent(Content content);

    @Insert
    void insertQuestion(Question questions);
    @Insert
    void insertQuiz(Quiz[] quiz);
    @Insert
    void insertNotification(Notification notifications);


    //External Database
    //From Web integration to sync SQLite
    @Query("Select * from tblsubject")
    LiveData<List<Subject>> getAllSubject();

    //From Web integration to sync SQLite
    @Query("Select * from tblchapter")
    LiveData<List<Chapter>> getAllChapter();

    //From Web integration  to sync SQLite
    @Query("Select * from tblContent ")
    LiveData<List<Content>> getAllContent();

    //From Web integration  to sync SQLite
    @Query("Select * from tblquestion")
    LiveData<List<Question>> getAllQuestion();
    @Query("Select * from tblQuiz")
    LiveData<List<Quiz>> getAllQuiz();

    //Internal Database
    //From SQLiteJSON
    @Query("Select * from tblsubject")
    List<Subject> getAllSubjectJson();

    @Query("Select * from tblchapter")
    List<Chapter> getAllChapterJson();

    @Query("Select * from tblcontent")
    List<Content> getAllContentJson();

    @Query("Select * from tblNotification")
    List<Notification> getNotification();

    @Query("Delete From tblquestion")
    void deleteAllquestion();

    @Query("Delete From tblsubject")
    void deleteAllsubject();
    @Query("Delete From tblNotification")
    void deleteAllNotification();


    @Query("Select * from tblchapter WHERE subjectID = :subjectId")
    List<Chapter> getChapterJson(int subjectId);

    @Query("Select * from tblQuiz where ContentID=:contentId")
    LiveData<List<Quiz>> getQuizJson(int contentId);

    @Query("Select * from tblQuiz where ContentID=:contentId")
    List<Quiz> getQuestionJson(int contentId);

    @Query("Select * from tblsubject")
    List<Subject> getSubjectJson();

}

