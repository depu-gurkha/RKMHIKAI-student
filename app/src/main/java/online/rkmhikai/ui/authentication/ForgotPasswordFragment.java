package online.rkmhikai.ui.authentication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;


public class ForgotPasswordFragment extends Fragment {

    TextView tvBckToSignIN;
    Button btnForgotPwd;
    EditText etEmail,etPhone;
    String email;
    public static FragmentManager fragmentManager;
    TextView txtMailSent,txtSuccess;
    ProgressBar progressBar;



    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_forgot_password, container, false);
        tvBckToSignIN=v.findViewById(R.id.tvBckToSignIn);
        btnForgotPwd=v.findViewById(R.id.pwdReset);
        etEmail=v.findViewById(R.id.email);
        etPhone=v.findViewById(R.id.phone);
        txtMailSent=v.findViewById(R.id.txtmailSent);
        progressBar=v.findViewById(R.id.progress_bar);
        txtSuccess=v.findViewById(R.id.success);
        btnForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=etEmail.getText().toString().trim();
                String check="^([a-zA-Z0-9.]+)@([a-zA-Z]+)\\.([a-zA-Z]+)$";

                if (email.isEmpty() | ! email.matches(check)){
                    Log.d("email",email);
                    etEmail.setError("Invalid Email");
                }
                else{
                    btnForgotPwd.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    forgotPwd();


                }


            }
        });

        //Phone TextWatcher
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", "beforeTextChanged: "+etPhone.length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", "onTextChanged: "+etPhone.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: "+etPhone.length());
                if(etPhone.length()>0){
                    etEmail.setText("");
                    etEmail.setEnabled(false);
                    //Log.d("TAG", "onTextChanged: "+etEmail.length());
                }else{
                    etEmail.setEnabled(true);
                }
            }
        });


        //Email TextWatcher
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", "beforeTextChanged: "+etEmail.length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("TAG", "onTextChanged: "+etEmail.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: "+etEmail.length());
                if(etEmail.length()>0){
                    etPhone.setText("");
                    etPhone.setEnabled(false);
                    //Log.d("TAG", "onTextChanged: "+etEmail.length());
                }else{
                    etPhone.setEnabled(true);
                }
            }
        });


        String text = "Back to Sign In Click Here";
        SpannableString sBck = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                fragmentManager=getFragmentManager();
                if(savedInstanceState!=null){
                    Log.d("Inside first if",fragmentManager.toString());
                    return;
                }
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                LoginFragment loginFragment=new LoginFragment();
                fragmentTransaction.replace(R.id.fragment_container,loginFragment,null);
                fragmentTransaction.commit();


            }
        };
        sBck.setSpan(fcsBlue, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBck.setSpan(clickableSpan, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBckToSignIN.setText(sBck);
        tvBckToSignIN.setMovementMethod(LinkMovementMethod.getInstance());
        return v;
    }

    public void forgotPwd(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.forgotPassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.d("Server Response", obj.toString());
//                            if no error in response
                            if (obj.getInt("success") == 1) {
                                progressBar.setVisibility(View.GONE);
                                btnForgotPwd.setVisibility(View.VISIBLE);
                                txtSuccess.setVisibility(View.VISIBLE);

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
                params.put("email", email);
                params.put("PhoneNo", "");
                return params;
            }
        };

        RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}