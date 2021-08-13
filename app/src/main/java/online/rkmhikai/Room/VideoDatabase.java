package online.rkmhikai.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import online.rkmhikai.Tables.Chapter;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.Tables.Notification;
import online.rkmhikai.Tables.Question;
import online.rkmhikai.Tables.Quiz;
import online.rkmhikai.Tables.Subject;


@Database(entities = {Subject.class, Chapter.class, Content.class, Question.class, Quiz.class, Notification.class},version = 1,exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {
    public static VideoDatabase instance;
    public abstract MyDAO myDAO();

    public static synchronized VideoDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    VideoDatabase.class,"MyDatabase").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
