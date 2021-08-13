package online.rkmhikai.ui.player.nestedlist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import online.rkmhikai.R;
import online.rkmhikai.Tables.Content;

public class ChapterAd extends RecyclerView.Adapter<ChapterAd.ChapterAdViewHolder> {
    Activity activity;
    private Context mContext;
    private List<ChapterDetail> chapterDetailsList;
    private List<Content> contents;

    RecyclerViewClickInterface recyclerViewClickInterface;

    public ChapterAd(Context mContext, List<ChapterDetail> chapterDetails, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.mContext = mContext;
        this.chapterDetailsList = chapterDetails;
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ChapterAdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chapter_item, parent, false);
        return new ChapterAdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdViewHolder holder, int position) {
        ChapterDetail chapterDetails = chapterDetailsList.get(position);
        holder.txtTotalLecture.setText("Total:"+String.valueOf(chapterDetails.getTotalLecture()));
        holder.txtChapterTitle.setText(chapterDetails.getChapterTitle());
        LectureAdapter lectureAdapter = new LectureAdapter(mContext, chapterDetails.getLectureList(),recyclerViewClickInterface);
//        Log.d("InsideChapter", contents.toString());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.rv_lecture.setLayoutManager(layoutManager);
        holder.rv_lecture.setAdapter(lectureAdapter);
        Log.d("lectureAdapter", "adapterCalled");
    }

    @Override
    public int getItemCount() {
        return chapterDetailsList.size();
    }

    public class ChapterAdViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapterTitle, txtTotalLecture;
        RecyclerView rv_lecture;
        public ChapterAdViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_lecture = itemView.findViewById(R.id.rv_lecture);
            txtChapterTitle = itemView.findViewById(R.id.tv_chapter_title);
            txtTotalLecture = itemView.findViewById(R.id.tv_total_lecture);
        }
    }
}
