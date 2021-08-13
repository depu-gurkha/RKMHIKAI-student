package online.rkmhikai.ui.authentication;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.library.Validation;
import online.rkmhikai.ui.splash.LoadActivity;

public class ActivationFragment extends Fragment {


    TextView txtNoEmail, txtUserName, txtEmail, txtHikaiId, txtPassword;
    Button btnResendEmail;
    ImageView ivEditEmail;
    ProgressBar resendProgress;
    String email,oldEmail;

    //JSON Array
    private JSONArray result;
    public JsonObjectRequest request;

    private int mInterval = 120000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private FragmentManager fragmentManager;


    public ActivationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activation, container, false);
        txtNoEmail = v.findViewById(R.id.tv_NoEmail);
        txtEmail = v.findViewById(R.id.tv_email_id);
        txtUserName = v.findViewById(R.id.tv_user_name);
        txtHikaiId = v.findViewById(R.id.tv_rkmhikai_id);
        txtPassword = v.findViewById(R.id.tv_password);
        btnResendEmail = v.findViewById(R.id.btn_resend_email);
        ivEditEmail = v.findViewById(R.id.iv_edit_email);
        resendProgress = v.findViewById(R.id.activation_progress);
        mHandler = new Handler();
        startRepeatingTask();


        String text = "Not getting the email? check your spam/junk folder. If you don't find it there within 10 minutes, do not hesitate to contact us";
        SpannableString sSignUp = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.RED);
        sSignUp.setSpan(fcsBlue, 0, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtNoEmail.setText(sSignUp);
        txtNoEmail.setMovementMethod(LinkMovementMethod.getInstance());

        getActivationDetails();

        btnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Resending Email....", Toast.LENGTH_SHORT).show();
                resendActivationEmail();
            }
        });
        ivEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText txt_inputText = mView.findViewById(R.id.txt_input);
                txt_inputText.setText(oldEmail);
                Button btn_cancel = mView.findViewById(R.id.btn_cancel);
                Button btn_okay = mView.findViewById(R.id.btn_okay);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String editedEmail = txt_inputText.getText().toString();
                        String check="^([a-zA-Z0-9.]+)@([a-zA-Z]+)\\.([a-zA-Z]+)$";
                        if(editedEmail.equals(oldEmail)|editedEmail.isEmpty() | !editedEmail.matches(check)){
                            Toast.makeText(getContext(), "Please Enter a Valid email", Toast.LENGTH_LONG).show();
                        }else {
                            Log.d("EDIT", "onClick: " + editedEmail);
                            oldEmail=editedEmail;
                            txtEmail.setText(editedEmail);
                            updateEmail();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });
        return v;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            getActivationDetails();
            // 100% guarantee that this always happens, even if
            // your update method throws an exception
            mHandler.postDelayed(mStatusChecker, mInterval);

        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void resendActivationEmail() {
        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();
         //String token="f163c127ce22d10f5a9a080f23ed82d6";
        email = txtEmail.getText().toString();
        if (token.equals("") || email.equals("")) {
            Toast.makeText(getContext(), "Can't Resend", Toast.LENGTH_LONG).show();
        } else {
            resendProgress.setVisibility(View.VISIBLE);
            btnResendEmail.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.resendActivationEmail,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("TAG", "onResponse: " + response);
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);
                                Log.d("Server Response", obj.toString());
                                // if no error in response
                                if (obj.getInt("success") == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    btnResendEmail.setVisibility(View.VISIBLE);
                                    resendProgress.setVisibility(View.GONE);
                                } else {
                                    btnResendEmail.setVisibility(View.VISIBLE);
                                    resendProgress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                btnResendEmail.setVisibility(View.VISIBLE);
                                resendProgress.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btnResendEmail.setVisibility(View.VISIBLE);
                            resendProgress.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("email", email);
//                    params.put("email", "depukaf@gmail.com");
                    return params;

                }
            };

            RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
        }

    }


    private void updateEmail() {
        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();
        //String token="f163c127ce22d10f5a9a080f23ed82d6";
        email = txtEmail.getText().toString();
        Log.d("ACTIVATION", "updateEmail: " + email);
        if (token.equals("") || email.equals("")) {
            Toast.makeText(getContext(), "Updating Email", Toast.LENGTH_LONG).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.updateEmail,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("TAG", "onResponse: " + response);
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);
                                Log.d("Server Response", obj.toString());
                                //  if no error in response
                                if (obj.getInt("success") == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    SharedPrefManager.getInstance(getActivity()).setUserEmail(email);
                                } else {

                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
//
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btnResendEmail.setVisibility(View.VISIBLE);
                            resendProgress.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("email", email);
//                    params.put("email", "depukaf@gmail.com");
                    return params;

                }
            };

            RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
        }

    }


    private void getActivationDetails() {

        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();
         //String token="f163c127ce22d10f5a9a080f23ed82d6";

        //Creating a string request
        request = new JsonObjectRequest(Request.Method.GET, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.getActivation + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;

                try {

                    result = response.getJSONArray("records");
                    Log.i("VOLLEY", "onResponse2: " + result.length());
                    Log.i("VOLLEY", "onResponse1: " + response.getJSONArray("records"));
                    Log.i("VOLLEY", "onResponse: " + result.getJSONObject(0).getString("email"));
                    txtEmail.setText(result.getJSONObject(0).getString("email"));
                    oldEmail=result.getJSONObject(0).getString("email");
                    txtHikaiId.setText(result.getJSONObject(0).getString("participantID"));
                    txtUserName.setText(result.getJSONObject(0).getString("firstName") + " " + result.getJSONObject(0).getString("lastName"));
                    txtPassword.setText(result.getJSONObject(0).getString("password"));
                    int regStage = result.getJSONObject(0).getInt("regStage");
                    if (regStage == 5) {
                        stopRepeatingTask();
                        fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ThanksFragment thanksFragment = new ThanksFragment();
                        fragmentTransaction.replace(R.id.fragment_container, thanksFragment, null);
                        fragmentTransaction.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "onErrorResponse: " + error.toString());
            }
        });

        int x=2;// retry count
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

}