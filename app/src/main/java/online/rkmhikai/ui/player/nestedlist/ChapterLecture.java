package online.rkmhikai.ui.player.nestedlist;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import online.rkmhikai.R;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.Tables.Chapter;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.ui.player.PlayerActivity;
import online.rkmhikai.ui.player.VideoViewMainFragment;


public class ChapterLecture extends Fragment implements RecyclerViewClickInterface{
    RecyclerView rvChapter;
    LinearLayoutManager layoutManagerGroup;
    ChapterAd adapterChapter;
    ArrayList<ChapterDetail> chap_list;
    ArrayList<Content> content_list;
    Repository repository;
    List<ChapterDetail> chapter_list=new ArrayList<>();

    int subjectId;
    String subjectTitle;

    public ChapterLecture() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chapter_lecture, container, false);
        rvChapter = v.findViewById(R.id.rv_chapter_lecture);

        PlayerActivity activity = (PlayerActivity) getActivity();
        subjectId = activity.getMySubject();
        subjectTitle = activity.getMySubjectTitle();


        layoutManagerGroup = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChapter.setLayoutManager(layoutManagerGroup);
        chap_list=new ArrayList<>();
        content_list = new ArrayList<>();
        repository = new Repository(getActivity().getApplication(),getActivity());

        List<ChapterDetail> res = null;
        try {
            res = getList(subjectId);
            Log.d("Getting Result",res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ChapterAd adapter=new ChapterAd(getContext(),res, ChapterLecture.this);
        rvChapter.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }

    public List<ChapterDetail> getList(int subjectId) throws JSONException {

        String result =repository.generateChapterJSON(subjectId);
        Log.d("HAmroBhai",result);
        JSONArray mainArray=new JSONArray(result);
        Log.d("HAmroBhai",mainArray.toString());
        for(int i=0;i<mainArray.length();i++){
            Log.d("Inside For","For Bithra");
            JSONObject jobject = mainArray.getJSONObject(i);
            String chapterTitle = jobject.getString("title");
            int totalLecture = jobject.getInt("items");;
            JSONArray lectureArray=jobject.getJSONArray("content");
            Log.d("Lecture",jobject.getJSONArray("content").toString());

            ChapterDetail chapter = new ChapterDetail(chapterTitle,totalLecture,buildSubItemList(lectureArray));
            Log.d("MyReturnValue",result);
            chapter_list.add(chapter);
        }
        Log.d("MyReturnValue",result);
        return chapter_list;

    }

    private List<Content> buildSubItemList(JSONArray lectureArray) {

            final List<Content> lectureList = new ArrayList<>();

            try {
                for (int i=0; i<lectureArray.length(); i++) {

                    JSONObject lectureObject=lectureArray.getJSONObject(i);

                        String lectureTitle = lectureObject.getString("title");
                        String type = lectureObject.getString("type");
                        int contentId=lectureObject.getInt("contentID");
                        String webUrl=lectureObject.getString("webUrl");
                        String localUrl=lectureObject.getString("localUrl");
                        Content lecture = new Content(lectureTitle, type,contentId,webUrl,localUrl);
                        lectureList.add(lecture);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lectureList;

    }

    @Override
    public void onItemClick(String name) {
        Log.d("CHAP", "onItemClick: "+name);
        VideoViewMainFragment f = new VideoViewMainFragment();
        //f.iniExoplayer("http://techslides.com/demos/sample-videos/small.mp4");
        Bundle bundle = new Bundle();
        Log.i("",name);
        bundle.putString("path",name);
        bundle.putLong("seek",0);
        bundle.putInt("Subject_ID",subjectId);
        bundle.putString("Subject_Title",subjectTitle);
        f.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_layout_main,f).commit();
    }


}