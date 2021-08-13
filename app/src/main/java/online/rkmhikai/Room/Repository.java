package online.rkmhikai.Room;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import online.rkmhikai.MainActivity;
import online.rkmhikai.Tables.Chapter;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.Tables.Notification;
import online.rkmhikai.Tables.Question;
import online.rkmhikai.Tables.Quiz;
import online.rkmhikai.Tables.Subject;

public class Repository {
    MyDAO myDAO;

    LiveData<List<Subject>> allSubjects;
    LiveData<List<Chapter>> allChapters;
    LiveData<List<Content>> allContents;
    LiveData<List<Question>> allQuestions;
    LiveData<List<Quiz>> allQuiz;
    int contentId;

    public  static Activity activity;


    public Repository (Application application, Activity activity){
        VideoDatabase database=VideoDatabase.getInstance(application);
        myDAO=database.myDAO();

        allSubjects=myDAO.getAllSubject();
        allChapters=myDAO.getAllChapter();
        allContents=myDAO.getAllContent();
        allQuestions=myDAO.getAllQuestion();
        allQuiz=myDAO.getQuizJson(contentId);
        this.activity=activity;

    }

    public void insertSubject(Subject subject){
        new InsertSubjectAsyncTask(myDAO).execute(subject);
    }

    public void insertChapter(Chapter chapter){
        new InsertChapterAsyncTask(myDAO).execute(chapter);
    }

    public void insertContent(Content content){
        new InsertContentAsyncTask(myDAO).execute(content);
    }

    public void insertQuiz(Quiz quiz) {
        new InsertQuizAsyncTask(myDAO).execute(quiz);
    }
    public void insertNotification(Notification notification){
        new InsertNoticationAsyncTask(myDAO).execute(notification);
    }

    public void insertQuestions(Question question){
        new InsertQuestionAsyncTask(myDAO).execute(question);
    }
    public void deleteAllQuestion(){
        new DeleteAllQuestionAsyncTask(myDAO).execute();
    }

    public void deleteAllsubject(){
        new DeleteAllSubjectAsyncTask(myDAO).execute();
    }

    public void deleteAllNotofication(){
        new DeleteAllNotificationAsyncTask(myDAO).execute();
    }





    //Generate Chapter JSON
    public String generateSubjectJSON()
    {
        String result=null;
        GenerateSubjectJsonAsyncTask task = new GenerateSubjectJsonAsyncTask(myDAO);
        try {
            result= task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("RES",result);
        return result;

    }

    //Generate Notication JSON
    public String generateNotificationJSON()
    {
        String result=null;
        GenerateNotificationJsonAsyncTask task = new GenerateNotificationJsonAsyncTask(myDAO);
        try {
            result= task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("RES",result);
        return result;

    }

    //Generate Chapter JSON
    public String generateChapterJSON(int subid)
    {
        String result=null;
        GenerateChapterJsonAsyncTask task = new GenerateChapterJsonAsyncTask(myDAO,subid);
        try {
            result= task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("RES",result);
        return result;

    }




    public LiveData<List<Subject>> getAllSubjects(){
        return allSubjects;
    }

    public LiveData<List<Chapter>> getAllChapters(){
        return allChapters;
    }

    public LiveData<List<Content>> getAllContent(){
        return allContents;
    }

    public LiveData<List<Question>> getAllQuestion(){
        return allQuestions;
    }



    public LiveData<List<Quiz>> getQuizJson(int contentId) {
        Log.d("QUIZTAG", "getQuizJson: Repository: "+contentId);
        this.contentId=contentId;
        return allQuiz;
    }



    public static class InsertSubjectAsyncTask extends AsyncTask<Subject,Void,Void>{
        private MyDAO myDAO;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progress.dismiss();
            Intent intent=new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();

        }

        private InsertSubjectAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Subject... subjects) {
            myDAO.insertSubject(subjects[0]);
            return null;
        }
    }

    public static class InsertQuizAsyncTask extends AsyncTask<Quiz, Void, Void> {
        private MyDAO myDAO;

        private InsertQuizAsyncTask(MyDAO myDAO) {
            this.myDAO = myDAO;
        }

        @Override
        protected Void doInBackground(Quiz... quizzes) {
            myDAO.insertQuiz(quizzes);
            return null;
        }
    }



    public static class InsertChapterAsyncTask extends AsyncTask<Chapter,Void,Void>{
        private MyDAO myDAO;

        private InsertChapterAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Chapter... chapters) {
            myDAO.insertChapter(chapters[0]);
            return null;
        }
    }

    public static class InsertContentAsyncTask extends AsyncTask<Content,Void,Void>{
        private MyDAO myDAO;

        private InsertContentAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Content... contents) {
            myDAO.insertContent(contents[0]);
            return null;
        }
    }

    public static class InsertQuestionAsyncTask extends AsyncTask<Question,Void,Void>{
        private MyDAO myDAO;

        private InsertQuestionAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Question... questions) {
            myDAO.insertQuestion(questions[0]);
            return null;
        }
    }

    public static class InsertNoticationAsyncTask extends AsyncTask<Notification,Void,Void>{
        private MyDAO myDAO;

        private InsertNoticationAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Notification... notifications) {
            myDAO.insertNotification(notifications[0]);
            return null;
        }
    }


    public static class GenerateChapterJsonAsyncTask extends AsyncTask<Void,Void,String>{
        private MyDAO myDAO;
        private int subid;
        public String value;


        //Subject Array
        JSONArray ch_ar = new JSONArray();
        public String  getValue(){
            return value;
        }


        private GenerateChapterJsonAsyncTask(MyDAO myDAO,int subid){
            this.myDAO=myDAO;
            this.subid=subid;
        }

        @Override
        protected String doInBackground(Void ...voids) {

            List<Chapter> chapterList = myDAO.getChapterJson(subid);

            List<Content> cn_list=myDAO.getAllContentJson();


            try {
                for (int i=0;i<chapterList.size();i++)
                {
                    Log.d("HELLO", "HELLO FROM Chapter: "+chapterList.get(i).getSubjectId());

                    //Chapter Object
                    JSONObject chapter_obj = new JSONObject();
                    chapter_obj.put("chapterID",chapterList.get(i).getChapterNo());
                    chapter_obj.put("subjectID",chapterList.get(i).getSubjectId());
                    chapter_obj.put("title",chapterList.get(i).getTitle());
                    chapter_obj.put("description",chapterList.get(i).getDesc());
                    chapter_obj.put("items",chapterList.get(i).getItems());
                    JSONArray cn_ar=new JSONArray();
                    for(int j=0;j<cn_list.size();j++){
                        Log.d("HELLO", "Content check"+cn_list.get(i).getChapterID()+" AND from chap"+cn_list.get(i).getTitle());
                        if(chapterList.get(i).getChapterNo() == cn_list.get(j).getChapterID()){
                            //Chapter Object
                            JSONObject cn_obj = new JSONObject();
                            Log.d("HELLO","HELLO from Content: "+cn_list.get(j).getTitle());
                            cn_obj.put("contentID",cn_list.get(j).getContentID());
                            cn_obj.put("chapterNo",cn_list.get(j).getChapterID());
                            cn_obj.put("title",cn_list.get(j).getTitle());
                            cn_obj.put("description",cn_list.get(j).getType());
                            cn_obj.put("webUrl",cn_list.get(j).getWebUrl());
                            cn_obj.put("localUrl",cn_list.get(j).getLocalUrl());

//                            Log.d("FILEINSERT1",l_list.get(j).getFile());
////                          String url="/storage/emulated/0/Android/data/com.demo.rkmlearn/files/Videos"+l_list.get(j).getFile().substring(43);
//                            String url=l_list.get(j).getFile().replace("https://rkmshillong.online/public/media/", "/storage/emulated/0/Android/data/com.demo.rkmlearn/files/Videos/");;
//                            l_obj.put("file",url);
//                            //Lecture Array
//                            Log.d("FILEINSERT2",url);
//                            l_ar.put(l_obj);
                            cn_obj.put("type",cn_list.get(j).getType());

                            cn_ar.put(cn_obj);
                        }
                    }
                    chapter_obj.put("content",cn_ar);
                    //c_obj.put("items",  String.valueOf(l_ar.length()));
                    ch_ar.put(chapter_obj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("HELLOJOSN",ch_ar.toString());
            return ch_ar.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            // Call activity method with results
            super.onPostExecute(result);
            value=result;

        }

    }


    public static class GenerateSubjectJsonAsyncTask extends AsyncTask<Void,Void,String>{
        private MyDAO myDAO;
        public String value;


        //Subject Array
        JSONArray subject_ar = new JSONArray();
        public String  getValue(){
            return value;
        }


        private GenerateSubjectJsonAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }

        @Override
        protected String doInBackground(Void ...voids) {

            List<Subject> subjectList = myDAO.getSubjectJson();
            Log.d("MYDAOSUBJECT", myDAO.getSubjectJson().toString());
            Log.d("HELLO", "Subject "+subjectList.toString());
            try {
                for (int i=0;i<subjectList.size();i++)
                {
                    Log.d("HELLO", "HELLO FROM Chapter: "+subjectList.get(i).getSubjectID());

                    //Chapter Object
                    JSONObject subject_obj = new JSONObject();
                    subject_obj.put("subjectID",subjectList.get(i).getSubjectID());
                    subject_obj.put("title",subjectList.get(i).getTitle());
                    subject_ar.put(subject_obj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("HELLOJOSN",subject_ar.toString());
            return subject_ar.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            // Call activity method with results
            super.onPostExecute(result);
            value=result;
        }

    }




    //Deleting all Chapters
    private static class DeleteAllQuestionAsyncTask extends AsyncTask<Void,Void,Void>{
        private MyDAO myDAO;
        private DeleteAllQuestionAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myDAO.deleteAllquestion();
            return null;
        }
    }

    //Deleting all Subject
    private static class DeleteAllSubjectAsyncTask extends AsyncTask<Void,Void,Void>{
        private MyDAO myDAO;
        private DeleteAllSubjectAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myDAO.deleteAllsubject();
            return null;
        }
    }
    //Deleting all Subject
    private static class DeleteAllNotificationAsyncTask extends AsyncTask<Void,Void,Void>{
        private MyDAO myDAO;
        private DeleteAllNotificationAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myDAO.deleteAllNotification();
            return null;
        }
    }

    //QUIZ

    //Generate Chapter JSON
    public String generateQuestionJSON(int contentId)
    {
        String result=null;
        GenerateQuestionJsonAsyncTask task = new GenerateQuestionJsonAsyncTask(myDAO,contentId);
        try {
            result= task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("RES",result);
        return result;

    }

    public static class GenerateQuestionJsonAsyncTask extends AsyncTask<Void,Void,String>{
        private MyDAO myDAO;
        private int contentId;
        public String value;


        //Subject Array
        JSONArray question_ar = new JSONArray();
        public String  getValue(){
            return value;
        }


        private GenerateQuestionJsonAsyncTask(MyDAO myDAO,int contentId){
            this.myDAO=myDAO;
            this.contentId=contentId;
        }

        @Override
        protected String doInBackground(Void ...voids) {

            List<Quiz> quizList = myDAO.getQuestionJson(contentId);



            try {
                for (int i=0;i<quizList.size();i++)
                {
                    Log.d("HELLO", "HELLO FROM Chapter: "+quizList.get(i).getId());

                    //Chapter Object
                    JSONObject quiz_obj = new JSONObject();
                    quiz_obj.put("id",quizList.get(i).getId());
                    quiz_obj.put("qContentId",quizList.get(i).getqContentId());
                    quiz_obj.put("question",quizList.get(i).getQuestion());
                    quiz_obj.put("option1",quizList.get(i).getOption1());
                    quiz_obj.put("option2",quizList.get(i).getOption2());
                    quiz_obj.put("option3",quizList.get(i).getOption3());
                    quiz_obj.put("option4",quizList.get(i).getOption4());
                    quiz_obj.put("correctAns",quizList.get(i).getCorrectAns());
                    question_ar.put(quiz_obj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("HELLOJOSN",question_ar.toString());
            return question_ar.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            // Call activity method with results
            super.onPostExecute(result);
            value=result;

        }

    }

    public static class GenerateNotificationJsonAsyncTask extends AsyncTask<Void,Void,String>{
        private MyDAO myDAO;
        public String value;


        //Subject Array
        JSONArray notification_ar = new JSONArray();
        public String  getValue(){
            return value;
        }


        private GenerateNotificationJsonAsyncTask(MyDAO myDAO){
            this.myDAO=myDAO;
        }

        @Override
        protected String doInBackground(Void ...voids) {

            List<Notification> notificationList = myDAO.getNotification();
            Log.d("MYDAOSUBJECT", myDAO.getNotification().toString());

            try {
                for (int i=0;i<notificationList.size();i++)
                {
                    Log.d("HELLO", "HELLO FROM Chapter: "+notificationList.get(i).getId());

                    //Chapter Object
                    JSONObject notification_obj = new JSONObject();
                    notification_obj.put("id",notificationList.get(i).getId());
                    notification_obj.put("title",notificationList.get(i).getTitle());
                    notification_obj.put("text",notificationList.get(i).getText());


                    notification_ar.put(notification_obj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("HELLOJOSN",notification_ar.toString());
            return notification_ar.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            // Call activity method with results
            super.onPostExecute(result);
            value=result;
        }

    }
}

