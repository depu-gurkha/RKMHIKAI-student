package online.rkmhikai.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import online.rkmhikai.ui.authentication.User;
import online.rkmhikai.ui.dashboard.DashBoard;


public class SharedPrefManager {
        private static final String SHARED_PREF_NAME = "participant";
        private static final String URL="oururl";
        private static final String ID = "sessionId";
        private static final String USERNAME = "keyUsername";
        private static final String USER_EMAIL="email";
        private static final String USER_TYPE="userType";
        private static final String USER_GENDER="gender";
        private static final String USER_CLASS="class";
        private static final String USER_STREAM="stream";
        private static final String USER_DOB="dob";
        private static final String USER_PROFILEPIC="profilepicture";
        private static final String ACCESS_TOKEN="accessToken";
        private static final String REFRESH_TOKEN="refreshToken";
        private static final String TOKEN_EXPIRY="tokenExpiry";
        private static final String USER_ID="userid";
        private static  final String USER_PHONE="phone";
        private static final String COURSE_ID="courseId";
        private static final String TOKEN="token";

        private static final String PARTICIPANT_ID="token";

        private static final String SERVER_ADDRESS="serverAddress";

        private static online.rkmhikai.config.SharedPrefManager mInstance;// Shared Preference Instance
        private static Context ctx;

        public SharedPrefManager(Context context) {
            ctx = context;
        }
         //This method creATES AN INSTANCE of shared preference
        public static synchronized online.rkmhikai.config.SharedPrefManager getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new online.rkmhikai.config.SharedPrefManager(context);
            }
            return mInstance;
        }

        public void setUrl(String url){
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String strUrl=url.toString();
            editor.putString(URL,strUrl);
            editor.apply();
        }

        // Setting userId for vital information use
        public void setUserId(int userId){
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(USER_ID,userId);
            editor.apply();
        }

        // Setting serverAddress
        public void setServerAddress(String serverAddress){
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SERVER_ADDRESS,serverAddress);
            editor.apply();
        }

    // Setting userPhonefor vital information use
    public void setUserPhone(String phone){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PHONE,phone);
        editor.apply();
    }
    public void setUserEmail(String email){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL,email);
        editor.apply();
    }
    //Course Id
    public void setCourseId(String courseId){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COURSE_ID,courseId);
        editor.apply();
    }

    //Getting the url
    public int  getUserId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_ID,0);
    }

    public String getServerAddress(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SERVER_ADDRESS,"");
    }

    public String  getUserEmail(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_EMAIL,"");
    }


    //Course Id
    public void setToken(String token){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN,token);
        editor.apply();
    }

    //Getting the url

    public String  getToken(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN," ");
    }
    public String  getUserPhone(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_PHONE,"");
    }
    public String  getUCourseId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COURSE_ID,"1042");
    }

        //Getting the url

//        public String  getUrl(){
//            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            return sharedPreferences.getString(URL,"http://192.168.1.120/");
//        }

        //this method will store the user data in shared preferences
        public void userLogin(User user) {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ID, user.getSession_id());
            editor.putString(USERNAME, user.getName());
            editor.putString(USER_GENDER,user.getGender());
            editor.putString(USER_CLASS,user.getStandard());
            editor.putString(USER_EMAIL,user.getEmail());
            editor.putString(USER_TYPE,user.getUserType());
            editor.putString(ACCESS_TOKEN,user.getAccessToken());
            editor.putString(TOKEN_EXPIRY,user.getTokenExpiry());
            editor.putString(REFRESH_TOKEN,user.getRefreshToken());
            editor.putString(USER_STREAM,user.getStream());
            editor.putString(USER_DOB,user.getDateOfBirth());
            editor.putString(USER_PROFILEPIC,user.getProfilePicture());
            editor.putString(PARTICIPANT_ID,user.getParticipantId());
            editor.apply();
        }

        //this method will check whether user is already logged in or not
        public boolean isLoggedIn() {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(USERNAME, null) != null;
        }

        //this method will give the logged in user
        public User getUser() {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new User(
                    sharedPreferences.getString(USER_EMAIL, null),
                    sharedPreferences.getString(ID, null),
                    sharedPreferences.getString(USERNAME,null),
                    sharedPreferences.getString(USER_TYPE ,null),
                    sharedPreferences.getString(USER_STREAM,null),
                    sharedPreferences.getString(USER_GENDER,null),
                    sharedPreferences.getString(USER_DOB, null),
                    sharedPreferences.getString(USER_PROFILEPIC,null),
                    sharedPreferences.getString(ACCESS_TOKEN ,null),
                    sharedPreferences.getString(REFRESH_TOKEN,null),
                    sharedPreferences.getString(TOKEN_EXPIRY,null),
                    sharedPreferences.getString(USER_CLASS, null),
                    sharedPreferences.getString(PARTICIPANT_ID,null)

            );
        }
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, DashBoard.class));
    }

}
