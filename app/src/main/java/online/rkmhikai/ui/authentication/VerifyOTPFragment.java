package online.rkmhikai.ui.authentication;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import pl.droidsonroids.gif.GifImageView;


public class VerifyOTPFragment extends Fragment {


    TextView txtResend;
    Button btnVerify, btnSaveNumber,resend;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CardView cardOTP, cardWaiting, cardVerifyNumber;
    String verificationID;
    ImageButton iVEdit;
    String otp = "";
    GifImageView loading;
    Button btnSave;
    private int status = 1;
    String phoneNo;
    EditText etNumberSet,txtPhone ;
    String  requestBody = null;
    TextView txtOtpSend;



    public VerifyOTPFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        // Inflate the layout for this fragment

        final EditText code1 = v.findViewById(R.id.firstNumber);
        final EditText code2 = v.findViewById(R.id.secondNumber);
        final EditText code3 = v.findViewById(R.id.thirdNumber);
        final EditText code4 = v.findViewById(R.id.fourthNumber);
        final EditText code5 = v.findViewById(R.id.fifthNumber);
        final EditText code6 = v.findViewById(R.id.sixthNumber);
        loading = v.findViewById(R.id.loading);
        btnVerify = v.findViewById(R.id.btn_verify);
        cardOTP = v.findViewById(R.id.cardOTP);
        cardWaiting = v.findViewById(R.id.cardWaiting);
        iVEdit = v.findViewById(R.id.ivButton);
        btnSaveNumber = v.findViewById(R.id.numberSave);
        etNumberSet = v.findViewById(R.id.numberSet);
        txtOtpSend=v.findViewById(R.id.otpSendPhone);
        resend=v.findViewById(R.id.resend);
        EditText[] otpTextViews = {code1, code2, code3, code4, code5, code6};
        txtResend = v.findViewById(R.id.tvResend);
        cardVerifyNumber = v.findViewById(R.id.cardVerifyNumber);
        txtPhone = v.findViewById(R.id.etPhoneEdit);
        phoneNo = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUserPhone();
        //Toast.makeText(getContext(), phoneNo, Toast.LENGTH_SHORT).show();
        Log.d("Phone Number", phoneNo);
        if (phoneNo.equals("")) {

            cardVerifyNumber.setVisibility(View.VISIBLE);
            cardWaiting.setVisibility(View.GONE);
        } else {
            txtPhone.setText(phoneNo);
            cardVerifyNumber.setVisibility(View.GONE);
            sendVerificationCodeToUser(phoneNo);
        }



        String text = "You can generate new code Resend Or Edit";
        SpannableString sResend = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (status == 1) {
                    cardOTP.setVisibility(View.GONE);
                    status = 0;
                    txtPhone.setText("");
                    cardVerifyNumber.setVisibility(View.VISIBLE);
                    etNumberSet.setText(phoneNo);
                   

                }
            }
        };
        sResend.setSpan(fcsBlue, 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sResend.setSpan(clickableSpan, 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtResend.setText(sResend);
        txtResend.setMovementMethod(LinkMovementMethod.getInstance());


        for (EditText currTextView : otpTextViews) {
            currTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nextTextView().requestFocus();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                public TextView nextTextView() {
                    int i;
                    for (i = 0; i < otpTextViews.length - 1; i++) {
                        if (otpTextViews[i] == currTextView)
                            return otpTextViews[i + 1];
                    }
                    return otpTextViews[i];
                }
            });
        }

        btnVerify.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                for (EditText currText : otpTextViews) {
                    otp = otp + currText.getText().toString();
                }

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, otp);
                Log.d("verification Code 1 ",credential.toString());
                verifyUser(credential);
                verificationCodeVerify();
//                //verfication


            }
        });
        iVEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 1) {
                    status = 0;
                    txtPhone.setText("");
                    cardVerifyNumber.setVisibility(View.VISIBLE);
                    etNumberSet.setText(phoneNo);
                    cardWaiting.setVisibility(View.GONE);

                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 1) {
                    status = 0;
                    txtPhone.setText("");
                    cardVerifyNumber.setVisibility(View.VISIBLE);
                    etNumberSet.setText(phoneNo);
                    cardWaiting.setVisibility(View.GONE);

                }
            }
        });

        btnSaveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNo=etNumberSet.getText().toString();
                SharedPrefManager.getInstance(getContext()).setUserPhone(phoneNo);
                txtPhone.setText(SharedPrefManager.getInstance(getContext()).getUserPhone());
                numberSend();

            }
        });

        return v;
    }

    //Phone Number Verification
    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phoneNo)       // Phone number to verify
                        .setTimeout(10L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            cardWaiting.setVisibility(View.GONE);
            cardOTP.setVisibility(View.VISIBLE);
            txtOtpSend.setText(phoneNo);
            verificationID=s;
            Log.d("verification Code",verificationID);
        }
    };


    //VerifyingOtp

    private void verifyUser(PhoneAuthCredential credential  ) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("Hello", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    public void numberSend() {
        String number = etNumberSet.getText().toString().trim();
        SharedPrefManager.getInstance(getContext()).setUserPhone(number);
        String userId = String.valueOf(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.phoneVerify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.d("Server Response", obj.toString());
//                            if no error in response
                            if (obj.getInt("success") == 1) {
                                phoneNo=SharedPrefManager.getInstance(getContext()).getUserPhone();
                                sendVerificationCodeToUser(phoneNo);
                                cardWaiting.setVisibility(View.VISIBLE);
                                cardVerifyNumber.setVisibility(View.GONE);
                                 sendVerificationCodeToUser(phoneNo);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", number);
                params.put("token", SharedPrefManager.getInstance(getContext()).getToken());
                return params;
            }
        };

        RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    public void verificationCodeVerify(){


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", SharedPrefManager.getInstance(getContext()).getUserId());
            jsonObject.put("phone", SharedPrefManager.getInstance(getContext()).getUserPhone());
            jsonObject.put("smsCode", String.valueOf(otp));

            JSONObject jsonObject2 = new JSONObject().put("input", jsonObject);
            requestBody=jsonObject2.toString();
            Log.d("our Resquest",requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.otpVerify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.d("Server Response", obj.toString());
//                            if no error in response
                            if (obj.getInt("success") == 1) {
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                VitalInfoFragment vitalInfoFragment = new VitalInfoFragment();
                                fragmentTransaction.replace(R.id.fragment_container, vitalInfoFragment, null);
                                fragmentTransaction.commit();

                                // sendVerificationCodeToUser(phoneNo);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {




            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", SharedPrefManager.getInstance(getContext()).getToken());
                params.put("phone", SharedPrefManager.getInstance(getContext()).getUserPhone());
                params.put("smsCode", String.valueOf(otp));

                return params;
            }


        };

        RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}