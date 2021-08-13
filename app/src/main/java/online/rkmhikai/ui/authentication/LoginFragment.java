package online.rkmhikai.ui.authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.rkmhikai.R;
import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.library.Validation;
import online.rkmhikai.ui.serverName.ServerNameFragment;
import online.rkmhikai.ui.splash.LoadActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment {

    private static final int PERMISSION_STORAGE_CODE = 1000;
    TextView tvSignup, tvForgotPwd;
    TextInputLayout lPass, lUserName;
    EditText email, pwd;
    Button btnLogin, btnGoogle;
    LoginButton btnFacebook;
    public static FragmentManager fragmentManager;
    FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String EMAIL = "email";
    private static final String TAG = "FACELOG";
    ImageView icon1;
    String profileUrl;

    private TextView textViewError;
    private CardView view8Char, viewUppercase, viewNumber, viewSymbol;
    private RelativeLayout lytVerify;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false;
    LinearProgressIndicator linearProgressIndicator;
    private int strength ;
    int progress;
    RelativeLayout relativeLayout;
    Repository repository;
    MyViewModel myViewModel;
    ProgressBar loginProgress;

    ImageView ivEditServer;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreate",this.getClass().toString());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        tvSignup = v.findViewById(R.id.tvsignup);
        tvForgotPwd = v.findViewById(R.id.tvForgotPwd);
        lUserName = v.findViewById(R.id.lUsername);
        lPass = v.findViewById(R.id.lPass);
        email = v.findViewById(R.id.uname);
        pwd = v.findViewById(R.id.pwd);
        btnLogin = v.findViewById(R.id.btn_login);
        loginProgress=v.findViewById(R.id.loginProgress);
        repository=new Repository(getActivity().getApplication(),getActivity());
        myViewModel=new MyViewModel(getActivity().getApplication(),getActivity());
        icon1=v.findViewById(R.id.ivicon1);
        // textViewError = v.findViewById(R.id.txt_password_error);
        relativeLayout=v.findViewById(R.id.lyt_password);
        view8Char = v.findViewById(R.id.lyt_verify_4_icon);
        viewUppercase = v.findViewById(R.id.lyt_verify_1_icon);
        viewNumber = v.findViewById(R.id.lyt_verify_2_icon);
        viewSymbol = v.findViewById(R.id.lyt_verify_3_icon);
        lytVerify = v.findViewById(R.id.btn_verify);
        textViewError=v.findViewById(R.id.txt_password);

        //Toast to show the server address
        Toast.makeText(getApplicationContext(), "Connected to: "+SharedPrefManager.getInstance(getApplicationContext()).getServerAddress(), Toast.LENGTH_LONG).show();

        //To Edit the Server Address
        ivEditServer = v.findViewById(R.id.iv_edit_server);
        ivEditServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EDITSERVER", "onClick: Edit Server");
                Toast.makeText(getContext(), "Clicked On Edit Server", Toast.LENGTH_SHORT).show();

                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("fragment_key","LoginFragment");
                ServerNameFragment serverNameFragment = new ServerNameFragment();
                serverNameFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, serverNameFragment, null);
                fragmentTransaction.commit();
            }
        });

        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    relativeLayout.setVisibility(View.GONE);
                    inputChange();
                } else {
                    Log.e("TAG", "e1 not focused");
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });
        linearProgressIndicator = v.findViewById(R.id.progressIndicator);
        //Call
        mCallbackManager = CallbackManager.Factory.create();

//        btnGoogle = v.findViewById(R.id.btn_googleRegis);
        String text = "Create a new Account SIGN UP";
        SpannableString sSignUp = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                fragmentManager = getFragmentManager();
                if (savedInstanceState != null) {
                    Log.d("Inside first if", fragmentManager.toString());
                    return;
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SignupFirstPageFragment signupFirstPageFragment = new SignupFirstPageFragment();
                fragmentTransaction.replace(R.id.fragment_container, signupFirstPageFragment, null);
                fragmentTransaction.commit();


            }
        };
        sSignUp.setSpan(fcsBlue, 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sSignUp.setSpan(clickableSpan, 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignup.setText(sSignUp);
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance());
        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                if (savedInstanceState != null) {
                    Log.d("Inside first if", fragmentManager.toString());
                    return;
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                fragmentTransaction.replace(R.id.fragment_container, forgotPasswordFragment, null);
                fragmentTransaction.commit();
                Toast.makeText(getActivity(), "ForgotPassword", Toast.LENGTH_SHORT).show();
            }
        });

        //LOgin Buttton
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        //Sigining in with Google

//        btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginRegistrationActivity activity = (LoginRegistrationActivity) getActivity();
//                if (activity instanceof LoginRegistrationActivity) {
//                    activity.signIn(2);
//                }
//            }
//        });
        return v;
    }

    private void inputChange() {

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

                registrationDataCheck();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void userLogin() {
        //first getting the values
        //first getting the values
        final String username = email.getText().toString();
        final String password = pwd.getText().toString();
        if (Validation.isEmpty(lUserName, "Enter a valid Username") | Validation.isValidPassword(lPass, "Please enter a valid Passsword")) {

            btnLogin.setVisibility(View.GONE);
            loginProgress.setVisibility(View.VISIBLE);
            //if everything is fine
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.userloginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);
                                Log.d("Server Response", obj.toString());

                                //if no error in response
                                Log.d("Server Response", String.valueOf(obj.getInt("success")));
                                if (obj.getInt("success") == 1) {

//                                    Toast.makeText(getActivity().getApplicationContext(), "Data Success", Toast.LENGTH_SHORT).show();

                                    SharedPrefManager.getInstance(getActivity().getApplicationContext()).setCourseId(obj.getString("courseID"));
                                    //getting the user from the response

                                    JSONObject userJson = obj.getJSONObject("data");
                                    Log.d("data", userJson.toString());
                                    //url for Profile Pic
                                   profileUrl= userJson.getString("profilepicture");

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                            //Permission Denied, Request it
                                            String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                            //show popup for runtime permission
                                            requestPermissions(permissions, PERMISSION_STORAGE_CODE);
                                        }
                                        else {
                                            //Permission Already granted, perform download
                                            startDownloading(profileUrl);
                                        }
                                    }
                                   startDownloading(profileUrl);
                                    //creating a new user object
                                    User user = new User(
                                            userJson.getString("email"),
                                            userJson.getString("session_id"),
                                            userJson.getString("firstName")+" "+ userJson.getString("lastName"),
                                            userJson.getString("userType"),
                                            userJson.getString("stream"),
                                            userJson.getString("gender"),
                                            userJson.getString("dateOfBirth"),
                                            userJson.getString("profilepicture"),
                                            userJson.getString("accesstoken"),
                                            userJson.getString("refreshtoken"),
                                            userJson.getString("access_token_expires_in"),
                                            userJson.getString("class"),
                                            userJson.getString("participantID")

                                    );
                                    Log.d("ouruser", user.getName().toString());

                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

                                    //starting the profile activity
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity().getApplicationContext(), LoadActivity.class));
                                }
                                else {
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    btnLogin.setVisibility(View.VISIBLE);
                                    loginProgress.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                btnLogin.setVisibility(View.VISIBLE);
                                loginProgress.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            loginProgress.setVisibility(View.GONE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", username);
                    params.put("password", password);
                    return params;
                }
            };

            RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
        }
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password=pwd.getText().toString().trim();
        strength=0;
        progress =0;
        if (password.length() >= 8) {
            isAtLeast8 = true;
            icon1.setImageResource(R.drawable.com_facebook_button_icon);
//            view8Char.setCardBackgroundColor(R.color.white);
            strength++;

        } else {
            isAtLeast8 = false;
            icon1.setImageResource(R.drawable.ic_baseline_check_24);
//            view8Char.setCardBackgroundColor(R.color.com_facebook_blue);
            //icon1.setImageResource(R.drawable.ic_baseline_check_24);

        }
        if (password.matches("(.*[a-z].*[A-Z])|([A-Z].*[a-z].*)")) {
            hasUppercase = true;
//            viewUppercase.setCardBackgroundColor(Color.GREEN);
            strength++;

        } else {
            hasUppercase = false;
            viewUppercase.setCardBackgroundColor(R.color.light_gray);

        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
//            viewNumber.setCardBackgroundColor(Color.GREEN);

            strength++;

        } else {
            hasNumber = false;
//            viewNumber.setCardBackgroundColor(R.color.light_gray);

        }
        if (password.matches(".*[\\^`~<,>\"'}{\\]\\[|)(;&*$%#@!:./?\\\\+=\\-_ ].*")) {
            hasSymbol = true;
//            viewSymbol.setCardBackgroundColor(Color.GREEN);
            strength++;

        } else {
            hasSymbol = false;
//            viewSymbol.setCardBackgroundColor(R.color.light_gray);

        }
        checkStatus();
    }

    @SuppressLint("SetTextI18n")
    private void checkStatus() {
        if (strength < 2) {
            textViewError.setText("Password Strength: Very Weak");
            progress+=25;
            linearProgressIndicator.setProgress(progress);
        } else if (strength == 2) {
            textViewError.setText("Password Strength: Weak");
            progress+=50;
            linearProgressIndicator.setProgress(progress);

        } else if(strength ==3) {
            textViewError.setText("Password Strength: Strong");
            progress+=75;
            linearProgressIndicator.setProgress(progress);

        }else{
            textViewError.setText("Password Strength: Very Strong");
            progress+=100;
            linearProgressIndicator.setProgress(progress);
        }
    }

    public void loadContent(){

        Toast.makeText(getContext(), "Loading.... from Load Content", Toast.LENGTH_SHORT).show();

    }

    private void startDownloading(String url){
        //get url/text from edit text

        //Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();

        Log.i("TAG", "startDownloading: "+url);
        Log.i("TAG", "Filename: "+ URLUtil.guessFileName(url,null,null));

        //create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //Allow types of network to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading File...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()); //get current timestamp as file name
//        request.setDestinationInExternalFilesDir(getApplicationContext(),"/Videos/abc","Hello-"+System.currentTimeMillis()+".jpg");
        request.setDestinationInExternalFilesDir(getApplicationContext(),"/ProfilePic/", URLUtil.guessFileName(url,null,null));



        //get download service and enque file
        DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue((request));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted from popup, perform download
                    startDownloading(profileUrl);
                }
                else {
                    //permission denied from popup, show error message
                    Toast.makeText(getContext(), "Permision Denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}