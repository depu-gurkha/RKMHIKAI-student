package online.rkmhikai.Room;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import online.rkmhikai.Tables.Chapter;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.Tables.Notification;
import online.rkmhikai.Tables.Question;
import online.rkmhikai.Tables.Quiz;
import online.rkmhikai.Tables.Subject;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;

public class MyViewModel extends AndroidViewModel implements ViewModelStoreOwner {
    private Repository repository;
    private LiveData<List<Subject>> allSubjects;
    private LiveData<List<Chapter>> allChapters;
    private LiveData<List<Content>> allContents;
    private LiveData<List<Question>> allQuestions;
    private LiveData<List<Quiz>> allQuizess;
    int contentId;
    public JsonObjectRequest request;



    public MyViewModel(@NonNull Application application, Activity activity) {
        super(application);
        repository=new Repository(application,activity);
        allSubjects=repository.getAllSubjects();
        allChapters=repository.getAllChapters();
        allContents=repository.getAllContent();
        allQuestions=repository.getAllQuestion();
//        allQuizess=repository.getAllQuiz();


    }


    public void getNotification(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        request = new JsonObjectRequest(Request.Method.GET, SharedPrefManager.getInstance(getApplication().getApplicationContext()).getServerAddress()+URLs.notificationUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;

                try {

                    jsonArray = response.getJSONArray("records");
                    Log.i("VOLLEY", "onResponse2: "+jsonArray.length());
                    Log.i("VOLLEY", "onResponse1: "+response.getJSONArray("records"));
                    JSONObject jobj = null;

                    for (int i=0;i<jsonArray.length();i++){
                        jobj = jsonArray.getJSONObject(i);
                        Log.i("VOLLEY", "onResponse: "+jobj.getString("image"));
                        int id=jobj.getInt("id");
                        String title= jobj.getString("title");
                        String text=jobj.getString("text");

                        Notification notification = new Notification(id,title,text);
                        insertNotification(notification);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

//            @Override
//            public void onResponse(JSONArray response) {
//
//                Log.i("VOLLEY", "onResponse: Inside RESPONSE");
//                JSONObject jsonObject = null;
//
//                for (int i=0;i<response.length(); i++){
//                    try {
//                        Log.i("VOLLEY", "onResponse: Inside for loop");
//                        jsonObject = response.getJSONObject(i);
////                        Notification notification = new Notification();
////                        notification.setTitle(jsonObject.getString("title"));
////                        notification.setText(jsonObject.getString("text"));
////                        lstNotification.add(notification);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                setuprecyclerview(lstNotification);
//
//            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "onErrorResponse: "+error.toString());
            }
        });

        requestQueue.add(request);
    }





    public void getVolleyDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        //Toast.makeText(getApplication(),path, Toast.LENGTH_LONG).show();

        String url = SharedPrefManager.getInstance(getApplication().getApplicationContext()).getServerAddress()+URLs.getCourseDetail+ SharedPrefManager.getInstance(getApplication().getApplicationContext()).getUCourseId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
//                                Toast.makeText(getApplication(), "Inside Volley", Toast.LENGTH_SHORT).show();

                                JSONObject obj = response.getJSONObject(i);
                                int subjectID = obj.getInt("subjectID");
                                String title = obj.getString("title");
                                String description = obj.getString("description");
                                int status=obj.getInt("status");
                                Subject subject = new Subject(subjectID, title, description,status);
                                insertSubject(subject);
                                JSONArray chapterArray = obj.getJSONArray("chapter");
                                for (int j = 0; j < chapterArray.length(); j++) {
                                    JSONObject chapterObject = chapterArray.getJSONObject(j);
                                    int chapterId = chapterObject.getInt("chapterID");
                                    int subjectId = chapterObject.getInt("subjectID");
                                    int items = chapterObject.getInt("items");
                                    String desc = chapterObject.getString("description");
                                    String chapterTitle = chapterObject.getString("title");
                                    Chapter chapter = new Chapter(chapterId, desc, chapterTitle, items, subjectId);
                                    insertChapter(chapter);
                                    JSONArray contentArray = chapterObject.getJSONArray("content");
                                    for (int k = 0; k < contentArray.length(); k++) {
                                        Log.d("checking", contentArray.toString());
                                        JSONObject contentObject = contentArray.getJSONObject(k);
                                        int id = contentObject.getInt("contentCode");
                                        int contentId = contentObject.getInt("contentID");
                                        int contentChapterId = contentObject.getInt("chapterID");
                                        String type = contentObject.getString("type");
                                        String contentTitle = contentObject.getString("title");
                                        String contentDesc = contentObject.getString("description");
                                        String webUrl = contentObject.getString("file");
                                        String localUrl = webUrl.replace(SharedPrefManager.getInstance(getApplication().getApplicationContext()).getServerAddress()+"/public/media/","/Android/data/online.rkmhikai/files/Videos/");
                                        Content content = new Content(id, contentId, contentChapterId, type, contentTitle, contentDesc, webUrl,localUrl);
                                        insertContent(content);
                                        if(type.equals("quiz")) {

                                            JSONArray quizArray = contentObject.getJSONArray("questions");
                                            for(int l=0;l<quizArray.length();l++){
                                                JSONObject quizObject=quizArray.getJSONObject(l);
                                                int qId=quizObject.getInt("questionID");
                                                int qContentID=quizObject.getInt("contentID");
                                                String question= quizObject.getString("question");
                                                String option1=quizObject.getJSONObject("answers").getString("a");
                                                String option2=quizObject.getJSONObject("answers").getString("b");
                                                String option3=quizObject.getJSONObject("answers").getString("c");
                                                String option4=quizObject.getJSONObject("answers").getString("d");
                                                String correctAns=quizObject.getString("correctAnswer");
                                                int ans=0;
                                                switch(correctAns){
                                                    case "a":
                                                        ans=1;
                                                        break;
                                                    case "b":
                                                        ans=2;
                                                        break;
                                                    case "c":
                                                        ans=3;
                                                        break;
                                                    case "d":
                                                        ans=4;
                                                        break;
                                                }
                                                Quiz quiz=new Quiz(qId,qContentID,question,option1,option2,option3,option4,ans);
                                                Log.d("Quiz",quiz.toString());
                                                insertQuiz(quiz);

                                            }

                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Error Loading Please Load Again", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }


    public void insertSubject(final Subject subject){

        repository.insertSubject(subject);
    }

    public void insertChapter(final Chapter chapter){

        repository.insertChapter(chapter);
    }

    public void insertContent(final Content content){

        repository.insertContent(content);
    }
    public void insertQuiz(final Quiz quiz) {
//        Toast.makeText(getApplication(), "Inserted Chapter", Toast.LENGTH_SHORT).show();
        repository.insertQuiz(quiz);
    }

    public void insertNotification(final Notification notification){
        //Toast.makeText(getApplication(), "Inserted Chapter", Toast.LENGTH_SHORT).show();
        repository.insertNotification(notification);
    }

    public LiveData<List<Subject>> getAllSubjects(){
        return allSubjects;
    }

    public LiveData<List<Chapter>> getAllChapters(){
        return allChapters;
    }

    public LiveData<List<Content>> getAllContents(){
        return allContents;
    }

    public LiveData<List<Question>> getAllQuestions(){
        return allQuestions;
    }

    public LiveData<List<Quiz>> getQuizJson(int contentId) {
        Log.d("QUIZTAG", "getQuizJson: MyViewModel: "+contentId);
        this.contentId=contentId;
        allQuizess=repository.getQuizJson(contentId);
        return allQuizess;

    }


    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return null;
    }
}
