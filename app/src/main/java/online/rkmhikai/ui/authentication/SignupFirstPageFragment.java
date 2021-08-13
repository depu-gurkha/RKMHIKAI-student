package online.rkmhikai.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import online.rkmhikai.MainActivity;
import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.library.Validation;


public class SignupFirstPageFragment extends Fragment {


    TextView tvSignin;
    TextInputLayout lFirstName, lLastName, lPhone, lEmail,lPwd,lConfirmPwd;
    EditText etEmail, etFstName, etLstName, etPhone,etPwd,etConfirmPwd;
    Button btnSignUp, btnGoogle;

    public FragmentManager fragmentManager;
    FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String EMAIL = "email";
    private static final String TAG="FACELOG";
    ProgressBar registrationProgress;

    //PASSWORD STRENGTH
    ImageView icon1;

    private TextView textViewError;
    private CardView view8Char, viewUppercase, viewNumber, viewSymbol;
    private RelativeLayout lytVerify;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false;
    LinearProgressIndicator linearProgressIndicator;
    private int strength ;
    int progress;
    RelativeLayout relativeLayout;



    public SignupFirstPageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup_first_page, container, false);
        tvSignin = v.findViewById(R.id.tvsignin);
        lFirstName = v.findViewById(R.id.lFname);
        lLastName = v.findViewById(R.id.lLname);
        lPhone = v.findViewById(R.id.lPhone);
        lEmail = v.findViewById(R.id.lEmail);
        lPwd=v.findViewById(R.id.lPassword);
        lConfirmPwd=v.findViewById(R.id.lConfirmPwd);
        etEmail = v.findViewById(R.id.email);
        etFstName = v.findViewById(R.id.fName);
        etLstName = v.findViewById(R.id.lName);
        etPhone = v.findViewById(R.id.phone);
        etPwd=v.findViewById(R.id.password);
        etConfirmPwd=v.findViewById(R.id.confirmPwd);
        btnSignUp = v.findViewById(R.id.btn_signUp);
//        btnGoogle =v.findViewById(R.id.btn_googleRegis);
        registrationProgress =v.findViewById(R.id.registrationProgress);
        mCallbackManager = CallbackManager.Factory.create();


        icon1=v.findViewById(R.id.ivicon1);
        // textViewError = v.findViewById(R.id.txt_password_error);
        relativeLayout=v.findViewById(R.id.lyt_password);
        view8Char = v.findViewById(R.id.lyt_verify_4_icon);
        viewUppercase = v.findViewById(R.id.lyt_verify_1_icon);
        viewNumber = v.findViewById(R.id.lyt_verify_2_icon);
        viewSymbol = v.findViewById(R.id.lyt_verify_3_icon);
        lytVerify = v.findViewById(R.id.btn_verify);
        textViewError=v.findViewById(R.id.txt_password);
        linearProgressIndicator = v.findViewById(R.id.progressIndicator);

        etFstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFstName.setError(null);
            }
        });

        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    inputChange();
                } else {
                    Log.e("TAG", "e1 not focused");
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });



//        btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginRegistrationActivity activity = (LoginRegistrationActivity) getActivity();
//                if (activity instanceof LoginRegistrationActivity) {
//                    activity.signIn(1);
//
//                }
//            }
//        });
        //For already registred along with on click to take back to the signin page
        String text = "Already Registered? SIGN IN";
        SpannableString sSignIn = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);

        //Clicking the blue colored with take to sign in Fragment
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                fragmentManager = getFragmentManager();
                if (savedInstanceState != null) {
                    Log.d("Inside first if", fragmentManager.toString());
                    return;
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.fragment_container, loginFragment, null);
                fragmentTransaction.commit();

            }
        };
        sSignIn.setSpan(fcsBlue, 20, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sSignIn.setSpan(clickableSpan, 20, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignin.setText(sSignIn);
        tvSignin.setMovementMethod(LinkMovementMethod.getInstance());

        //Onclick to navigate to the next signup page
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }
        });
        return v;
    }

    //function For User Registration
    private void userRegistration() {

        final String firstName = etFstName.getText().toString().trim();
        final String lastName = etLstName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String password = etPwd.getText().toString().trim();
        final String conFirmPassword=etConfirmPwd.getText().toString().trim();

        if (Validation.isEmpty(lFirstName, "Please Enter Valid First Name") | Validation.isEmpty(lLastName, "Please Enter Valid Last Name") | Validation.isValidPassword(lPwd, "Password must contain at least one Capital letter, one small letter, a special character, and a number") |
                Validation.isValidPhone(lPhone, "Enter a valid Phone") | Validation.isValidEmail(lEmail, "Please enter a valid email") | Validation.isValidText(lLastName, "Invalid First Name") |
                Validation.isValidText(lFirstName, "Invalid Last Name") | conFirmPassword.isEmpty()) {
            if (!conFirmPassword.equals(password)) {
                lConfirmPwd.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
                lConfirmPwd.setError("Password did not match");
            } else {
                btnSignUp.setVisibility(View.GONE);
                registrationProgress.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.userRegisterUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("Server Response", obj.toString());
                                    //if no error in response

                                    if (obj.getInt("success") == 1) {
                                        Log.d("InSideSuccess", "Success");
//                                            Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        Log.d("Stage", String.valueOf(obj.getInt(
                                                "registrationStage")));
                                        Log.d("inside Switch", "helo");
                                        switch (obj.getInt("registrationStage")) {

                                            case 2:
                                                VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
                                                fragmentTransaction.replace(R.id.fragment_container, verifyOTPFragment, null);
                                                fragmentTransaction.commit();
                                                break;
                                            case 3:
                                                VitalInfoFragment vitalInfoFragment = new VitalInfoFragment();
                                                fragmentTransaction.replace(R.id.fragment_container, vitalInfoFragment, null);
                                                fragmentTransaction.commit();
                                                break;
                                            case 4:
                                                UploadImageFragment uploadImageFragment = new UploadImageFragment();
                                                fragmentTransaction.replace(R.id.fragment_container, uploadImageFragment, null);
                                                fragmentTransaction.commit();
                                                break;
                                            case 5:
                                                ActivationFragment activationFragment = new ActivationFragment();
                                                fragmentTransaction.replace(R.id.fragment_container, activationFragment, null);
                                                fragmentTransaction.commit();
                                                break;
                                        }

                                        //userId is the token
                                        String token = obj.getString("token");
                                        Log.d("userId", String.valueOf(token));
                                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).setToken(token);
                                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).setUserPhone(phone);
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
                                        btnSignUp.setVisibility(View.VISIBLE);
                                        registrationProgress.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    btnSignUp.setVisibility(View.VISIBLE);
                                    registrationProgress.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                btnSignUp.setVisibility(View.VISIBLE);
                                registrationProgress.setVisibility(View.GONE);
                                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("firstName", firstName);
                        params.put("lastName", lastName);
                        params.put("email", email);
                        params.put("phone", phone);
                        params.put("password", password);
                        params.put("registerMode", "AndroidApp");
                        return params;
                    }
                };

                RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();

//        if(currentUser !=null){
//            updateUI();
//        }
//        updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("yoho", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("yoho", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getEmail();
                            user.getPhoneNumber();
                            Log.d ("myuser",user.getProviderData().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        updateUI();

                        // ...
                    }
                });
    }

    private void updateUI(){

        Toast.makeText(getContext(),"You are Logged in",Toast.LENGTH_SHORT).show();
        Intent accountIntent= new Intent(getContext(), MainActivity.class);
        startActivity(accountIntent);

    }

    private void inputChange() {

        etPwd.addTextChangedListener(new TextWatcher() {
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

    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password=etPwd.getText().toString().trim();
        strength=0;
        progress =0;
        if (password.length() >= 8) {
            isAtLeast8 = true;
            icon1.setImageResource(R.drawable.when_answer_correct);
             //  view8Char.setCardBackgroundColor(R.color.white);
            strength++;

        } else {
            isAtLeast8 = false;
            //icon1.setImageResource(R.drawable.ic_baseline_check_24);
           // view8Char.setCardBackgroundColor(R.color.bg_Color);
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
}