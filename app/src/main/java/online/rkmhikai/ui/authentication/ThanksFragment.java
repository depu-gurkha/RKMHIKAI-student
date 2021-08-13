package online.rkmhikai.ui.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import online.rkmhikai.R;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;


public class ThanksFragment extends Fragment {

    TextView tvFirstName, tvLastName,tvParticipantId;
    Button btnGotoClassRoom;
    private FragmentManager fragmentManager;

    //JSON Array
    private JSONArray result;
    private JsonObjectRequest request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_thanks, container, false);
        tvFirstName = v.findViewById(R.id.tv_first_name);
        tvLastName = v.findViewById(R.id.tv_last_name);
        tvParticipantId = v.findViewById(R.id.tv_participantid);
        btnGotoClassRoom = v.findViewById(R.id.btn_goto_classroom);

        btnGotoClassRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        return v;
    }


    private void getActivationDetails() {

        String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken();
//        String token="5b68425d9497f5b7b5c2868d86f5060f";

        //Creating a string request
        request = new JsonObjectRequest(Request.Method.GET, SharedPrefManager.getInstance(getContext()).getServerAddress()+URLs.getActivation+token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;

                try {

                    result = response.getJSONArray("records");
                    Log.i("VOLLEY", "onResponse2: "+result.length());
                    Log.i("VOLLEY", "onResponse1: "+response.getJSONArray("records"));
                    Log.i("VOLLEY", "onResponse: "+result.getJSONObject(0).getString("email"));
                    tvFirstName.setText(result.getJSONObject(0).getString("firstName"));
                    tvLastName.setText(result.getJSONObject(0).getString("lastName"));
                    tvParticipantId.setText(result.getJSONObject(0).getString("participantID"));
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
}