package online.rkmhikai.ui.notification;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import online.rkmhikai.R;
import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.Tables.Notification;
import online.rkmhikai.Tables.Subject;
import online.rkmhikai.config.NotificationAdapter;
import online.rkmhikai.config.URLs;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    public JsonObjectRequest request;
    private RequestQueue requestQueue;
    LinearLayoutManager layoutManagerGroup;
    private ArrayList<Notification> lstNotification;
    private RecyclerView recyclerView;

    //JSON DATA for SUBJECT
    String notification_JSON;

    Repository repository;
    MyViewModel viewModel;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.notification_fragment, container, false);

        recyclerView = view.findViewById(R.id.rv_notification);
        layoutManagerGroup = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerGroup);

        viewModel = new MyViewModel(getActivity().getApplication(), getActivity());
        repository = new Repository(getActivity().getApplication(), getActivity());

        notification_JSON = repository.generateNotificationJSON();

        //lstNotification = new ArrayList<>();

        NotificationAdapter myadapter = new NotificationAdapter(getContext(),buildNotificationList());
       //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();


        //jsonrequest();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        // TODO: Use the ViewModel
    }

//    private void jsonrequest() {
//        request = new JsonObjectRequest(Request.Method.GET, URLs.notificationUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                JSONArray jsonArray = null;
//
//                try {
//
//                    jsonArray = response.getJSONArray("records");
//                    Log.i("VOLLEY", "onResponse2: "+jsonArray.length());
//                    Log.i("VOLLEY", "onResponse1: "+response.getJSONArray("records"));
//                    JSONObject jobj = null;
//
//                    for (int i=0;i<jsonArray.length();i++){
//                        jobj = jsonArray.getJSONObject(i);
//                        Log.i("VOLLEY", "onResponse: "+jobj.getString("image"));
////                       // Notification notification = new Notification();
////                        notification.setTitle(jobj.getString("title"));
////                        notification.setText(jobj.getString("text"));
////                        notification.setImgNotice(jobj.getString("image"));
////                        lstNotification.add(notification);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                setuprecyclerview(lstNotification);
//
//
//            }
//
////            @Override
////            public void onResponse(JSONArray response) {
////
////                Log.i("VOLLEY", "onResponse: Inside RESPONSE");
////                JSONObject jsonObject = null;
////
////                for (int i=0;i<response.length(); i++){
////                    try {
////                        Log.i("VOLLEY", "onResponse: Inside for loop");
////                        jsonObject = response.getJSONObject(i);
//////                        Notification notification = new Notification();
//////                        notification.setTitle(jsonObject.getString("title"));
//////                        notification.setText(jsonObject.getString("text"));
//////                        lstNotification.add(notification);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////
////                setuprecyclerview(lstNotification);
////
////            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("VOLLEY", "onErrorResponse: "+error.toString());
//            }
//        });
//
//        requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);
//    }

    private List<Notification> buildNotificationList() {

        final List<Notification> notificationList = new ArrayList<>();

        try {
            JSONArray notificationArray = new JSONArray(notification_JSON);
            Log.d("TAG", "buildNotificationList: "+notification_JSON);
            for(int i=0;i<notificationArray.length();i++){
                JSONObject notificationObject = notificationArray.getJSONObject(i);
                //Parent Recycler VieW
                int notificationId = notificationObject.getInt("id");
                String notificationTitle = notificationObject.getString("title");
                String notificationText = notificationObject.getString("text");
                Notification notification = new Notification(notificationId,notificationTitle,notificationText);

                notificationList.add(notification);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return notificationList;
    }



    private void setuprecyclerview(ArrayList<Notification> lstNotification) {



    }

}