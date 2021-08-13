package online.rkmhikai.ui.classroom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import online.rkmhikai.R;
import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.Tables.Subject;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.SubjectAdapter;

public class ClassRoomFragment extends Fragment {
    //JSON DATA for SUBJECT
    String subject_JSON;

    RecyclerView rvSubject;

    Repository repository;
    MyViewModel viewModel;

    LinearLayoutManager layoutManager;

    SubjectAdapter subjectAdapter;

    List<Subject> subjects;

    TextView tvEmptyRecord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private ClassRoomViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
                new ViewModelProvider(this).get(ClassRoomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_classroom, container, false);
        // get the reference of RecyclerView
        rvSubject =root.findViewById(R.id.rv_subject);
        tvEmptyRecord =root.findViewById(R.id.tv_empty_record);

        viewModel = new MyViewModel(getActivity().getApplication(), getActivity());
        repository = new Repository(getActivity().getApplication(), getActivity());

        subject_JSON = repository.generateSubjectJSON();
        Log.d("HELLOJSON1", "onCreate: "+subject_JSON);

        layoutManager = new LinearLayoutManager(getContext());

        subjects = new ArrayList<>();

        subjectAdapter = new SubjectAdapter(buildSubjectList(),getContext());


        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        rvSubject.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        Log.d("TAG", "onCreate: "+subjectAdapter.getItemId(1));
        //rvSubject.setLayoutManager(layoutManager);
        rvSubject.setAdapter(subjectAdapter);

        if (subjectAdapter.getItemCount()==0){
            Log.d("TAG", "onCreateView: SubjectAdapter Empty");
            tvEmptyRecord.setVisibility(View.VISIBLE);
        }


        return root;
    }

    private List<Subject> buildSubjectList() {

        final List<Subject> subjectList = new ArrayList<>();

        try {
            JSONArray subjectArray = new JSONArray(subject_JSON);

            for(int i=0;i<subjectArray.length();i++){
                JSONObject subjectObject = subjectArray.getJSONObject(i);
                //Parent Recycler VieW
                int subjectId = subjectObject.getInt("subjectID");
                String subjectTitle = subjectObject.getString("title");
                Subject subject = new Subject(subjectId,subjectTitle);
                Log.d("TAG", "buildSubjectList: "+subject.getSubjectID());
                Log.d("TAG", "buildSubjectList: "+subject.getTitle());
                subjectList.add(subject);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "buildSubjectList: "+subjectList.get(1));
        return subjectList;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem=menu.findItem(R.id.action_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search Subject");

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                subjectAdapter.getFilter().filter(s);

                return false;
            }
        });
    }
}