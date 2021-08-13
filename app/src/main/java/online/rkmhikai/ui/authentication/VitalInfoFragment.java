package online.rkmhikai.ui.authentication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.VoiceInteractor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import online.rkmhikai.R;
import online.rkmhikai.config.RequestSingletonVolley;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.library.Validation;


public class VitalInfoFragment extends Fragment {

    //An ArrayList for Spinner Items
    private ArrayList<String> schools;
    private ArrayList<String> genders;
    private ArrayList<String> classes;
    private ArrayList<String> streams;

    //JSON Array
    private JSONArray result;

    public JsonObjectRequest request;
    TextView txtdte,txtSchoolError;
    private DatePickerDialog datePicker;
    private int year, month, day;
    String gender="",standard="",stream="",DOB="",school="";
    Button btnSignUp;
    TextInputLayout dobError;
    public FragmentManager fragmentManager;
    //EditText Spinner
    EditText etSchool,etGender,etClass,etStream;
    Dialog dialog;
    TextInputLayout lSchool,lGender,lClass,lStream,lDob;
    ProgressBar vitalProgress;


    public VitalInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_vital_info, container, false);

        //Initializing the ArrayList
        schools = new ArrayList<String>();
        classes = new ArrayList<String>();
        genders = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Gender)));
        streams = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stream)));



        txtdte=v.findViewById(R.id.dtepicker);
        btnSignUp=v.findViewById(R.id.btn_signUp);

        txtSchoolError=v.findViewById(R.id.txtScError);

        lSchool=v.findViewById(R.id.lSchool);
        lGender=v.findViewById(R.id.lGender);
        lClass=v.findViewById(R.id.lClass);
        lStream=v.findViewById(R.id.lStream);
        lDob=v.findViewById(R.id.lDob);

        vitalProgress=v.findViewById(R.id.vitalProgress);
        ////EditText Spinner
        etSchool = v.findViewById(R.id.et_school);
        etGender = v.findViewById(R.id.et_gender);
        etClass = v.findViewById(R.id.et_class);
        etStream = v.findViewById(R.id.et_stream);

        
        etSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "Click on EditText Spinner", Toast.LENGTH_SHORT).show();
                //Initialize dialog
                dialog = new Dialog(getContext());
                //set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                //Set custom height and width
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show dialog
                dialog.show();

                //initialize and assign the variable
                TextView spTextView = dialog.findViewById(R.id.sp_text_view);
                EditText spEditText = dialog.findViewById(R.id.sp_edit_text);
                ListView spListView = dialog.findViewById(R.id.sp_list_view);
                TextView spTvError = dialog.findViewById(R.id.sp_tv_error);


                //Initialize the array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,schools);

                //set adapter
                spListView.setAdapter(adapter);


                spEditText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter.getCount()==0){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "beforeTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter==null)
                            return;
                        adapter.getFilter().filter(charSequence.toString());
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "onTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(adapter.getCount()<1){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "afterTextChanged: "+adapter.getCount());
                    }
                });




                spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(adapter.getItem(i).equals("Select School")){
                            etSchool.setText("");
                        }else {
                            etSchool.setText(adapter.getItem(i));
                        }
                        school=etSchool.getText().toString();
                        etSchool.setHint("Select School");
                        dialog.dismiss();

                        //Toast.makeText(getContext(), etSchool.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        etSchool.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "Click on Gender EditText Spinner", Toast.LENGTH_SHORT).show();
                //Initialize dialog
                dialog = new Dialog(getContext());
                //set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                //Set custom height and width
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show dialog
                dialog.show();

                //initialize and assign the variable
                TextView spTextView = dialog.findViewById(R.id.sp_text_view);
                EditText spEditText = dialog.findViewById(R.id.sp_edit_text);
                ListView spListView = dialog.findViewById(R.id.sp_list_view);
                TextView spTvError = dialog.findViewById(R.id.sp_tv_error);


                spTextView.setText("Click on the gender to select");
                spEditText.setVisibility(View.GONE);

                //Initialize the array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,genders);

                //set adapter
                spListView.setAdapter(adapter);


                spEditText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter.getCount()==0){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "beforeTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter==null)
                            return;
                        adapter.getFilter().filter(charSequence.toString());
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "onTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(adapter.getCount()<1){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "afterTextChanged: "+adapter.getCount());
                    }
                });




                spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(adapter.getItem(i).equals("Select Gender")){
                             etGender.setText("");
                        }else {
                            etGender.setText(adapter.getItem(i));
                        }
                        gender=etGender.getText().toString();
                        etGender.setHint("Select Gender");
                        dialog.dismiss();

                        //Toast.makeText(getContext(), etSchool.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        etClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "Click on Class EditText Spinner", Toast.LENGTH_SHORT).show();
                //Initialize dialog
                dialog = new Dialog(getContext());
                //set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                //Set custom height and width
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show dialog
                dialog.show();

                //initialize and assign the variable
                TextView spTextView = dialog.findViewById(R.id.sp_text_view);
                EditText spEditText = dialog.findViewById(R.id.sp_edit_text);
                ListView spListView = dialog.findViewById(R.id.sp_list_view);
                TextView spTvError = dialog.findViewById(R.id.sp_tv_error);


                spTextView.setText("Click on the class to select it");

                //Initialize the array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,classes);

                //set adapter
                spListView.setAdapter(adapter);


                spEditText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter.getCount()==0){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//                            spListView.setVisibility(View.VISIBLE);
//                            spTvError.setVisibility(View.GONE);
                        }
                        Log.d("TAG", "beforeTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter==null)
                            return;
                        adapter.getFilter().filter(charSequence.toString());
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "onTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(adapter.getCount()<1){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "afterTextChanged: "+adapter.getCount());
                    }
                });




                spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(adapter.getItem(i).equals("Select Class")){
                            etClass.setText("");
                        }else {
                            etClass.setText(adapter.getItem(i));
                            if(etClass.getText().toString().equals("Class XI") || etClass.getText().toString().equals("Class XII")){
                                etStream.setClickable(true);
                            }else {
                                etStream.setText("NA");
                                stream="NA";
                                etStream.setEnabled(false);
                            }
                        }
                        standard=etClass.getText().toString();
                        etClass.setHint("Select Class");
                        dialog.dismiss();
                        //Toast.makeText(getContext(), etClass.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        etStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "Click on Stream EditText Spinner", Toast.LENGTH_SHORT).show();
                //Initialize dialog
                dialog = new Dialog(getContext());
                //set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                //Set custom height and width
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show dialog
                dialog.show();

                //initialize and assign the variable
                TextView spTextView = dialog.findViewById(R.id.sp_text_view);
                EditText spEditText = dialog.findViewById(R.id.sp_edit_text);
                ListView spListView = dialog.findViewById(R.id.sp_list_view);
                TextView spTvError = dialog.findViewById(R.id.sp_tv_error);


                spTextView.setText("Click on the Stream to select");
                spEditText.setVisibility(View.GONE);

                //Initialize the array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,streams);

                //set adapter
                spListView.setAdapter(adapter);


                spEditText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter.getCount()==0){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//                            spListView.setVisibility(View.VISIBLE);
//                            spTvError.setVisibility(View.GONE);
                        }
                        Log.d("TAG", "beforeTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(adapter==null)
                            return;
                        adapter.getFilter().filter(charSequence.toString());
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "onTextChanged: "+adapter.getCount());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(adapter.getCount()<1){
                            Log.d("TAG", "onTextChanged: Inside");
//                            spListView.setVisibility(View.GONE);
                            spTvError.setVisibility(View.VISIBLE);
                        }else{
//
                        }
                        Log.d("TAG", "afterTextChanged: "+adapter.getCount());
                    }
                });




                spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if(adapter.getItem(i).equals("Select Stream")){
                            etStream.setText("");
                            stream="NA";
                        }else {
                            etStream.setText(adapter.getItem(i));
                            stream=etStream.getText().toString();
                        }
                        etStream.setHint("Select Stream");
                        dialog.dismiss();

                        //Toast.makeText(getContext(), etStream.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




        txtdte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                DOB=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                txtdte.setText(DOB);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });



        //This method will fetch the data from the URL
        getSchoolData();

        //This method will fetch the data from the URL
        getClassData();







        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVitalInfo();
            }
        });


        return v;
    }


    private void getSchoolData(){
        //Creating a string request
            request = new JsonObjectRequest(Request.Method.GET, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.getSchoolUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;

                try {

                    result = response.getJSONArray("records");
                    Log.i("VOLLEY", "onResponse2: "+result.length());
                    Log.i("VOLLEY", "onResponse1: "+response.getJSONArray("records"));

                    getSchool(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "onErrorResponse: "+error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void getSchool(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                schools.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("TAG", "getSchool:1 "+schools);

        schools.add(0,"Select School");
    }


    private void getClassData(){
        //Creating a string request
        request = new JsonObjectRequest(Request.Method.GET, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.getClassUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;

                try {

                    result = response.getJSONArray("records");
                    Log.i("VOLLEY", "onResponse2: "+result.length());
                    Log.i("VOLLEY", "onResponse1: "+response.getJSONArray("records"));

                    getClasses(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY", "onErrorResponse: "+error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void getClasses(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                classes.add(json.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("TAG", "getClasses:2 "+classes);

        classes.add(0,"Select Class");
        classes.add(1,"Class XI");

    }


    //User VitalInfo
    private void userVitalInfo() {
        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();



        if (Validation.isEmpty(lStream, "Select Valid Stream") | Validation.isEmpty(lDob,"Select Valid Date") | Validation.isEmpty(lSchool,"Please select a school")|Validation.isEmpty(lClass,"Please enter your class")|Validation.isEmpty(lGender,"Please select your gender")) {
            Log.d("TAG", "userVitalInfo:" + lStream.getEditText().getText().toString());
            Log.d("TAG", "userVitalInfo:" + lClass.getEditText().getText().toString());
            Log.d("TAG", "userVitalInfo:" + lDob.getEditText().getText().toString());
            Log.d("TAG", "userVitalInfo:" + lSchool.getEditText().getText().toString());
            Log.d("TAG", "userVitalInfo: " + lStream.getEditText().getText().toString());

            Log.d("ACTUAL", "userVitalInfo(STREAM): "+stream);
            Log.d("ACTUAL", "userVitalInfo(STANDARD): "+standard);
            Log.d("ACTUAL", "userVitalInfo(DOB): "+DOB);
            Log.d("ACTUAL", "userVitalInfo(SCHOOL): "+school);
            Log.d("ACTUAL", "userVitalInfo(GENDER): "+gender);
            Log.d("ACTUAL", "userVitalInfo(TOKEN): "+token);




            //Toast.makeText(getContext(), "Please select Valid Fields", Toast.LENGTH_SHORT).show();

//            String userId = String.valueOf(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUserId());
            //Log.d("USER ID", token);
            btnSignUp.setVisibility(View.GONE);
            vitalProgress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.vitalInfoUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("TAG", "onResponse: "+response);
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);
                                Log.d("Server Response", obj.toString());
                           // if no error in response
                                if (obj.getInt("success") == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), gender + standard + stream + DOB, Toast.LENGTH_SHORT).show();
                                    fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    UploadImageFragment uploadImage = new UploadImageFragment();
                                    fragmentTransaction.replace(R.id.fragment_container, uploadImage, null);
                                    fragmentTransaction.commit();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    btnSignUp.setVisibility(View.VISIBLE);
                                    vitalProgress.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                btnSignUp.setVisibility(View.VISIBLE);
                                vitalProgress.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            btnSignUp.setVisibility(View.VISIBLE);
                            vitalProgress.setVisibility(View.GONE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("dob", DOB);
                    params.put("gender", gender);
                    params.put("class", standard);
                    params.put("stream", stream);
                    params.put("school", school);
                    //params.put("token", "a54884f19656709f6afd1e2cbfbff11e");
                    params.put("token", token);
                    return params;

                }
            };

            RequestSingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);
        }
        else{
            Toast.makeText(getContext(), "Please enter valid fields", Toast.LENGTH_SHORT).show();
        }
    }
}