package online.rkmhikai.config;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import online.rkmhikai.R;
import online.rkmhikai.Tables.Subject;
import online.rkmhikai.ui.player.PlayerActivity;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> implements Filterable {

    List<Subject> subjectList = new ArrayList<>();
    List<Subject> filteredList = new ArrayList<>();
    CustomFilter customFilter;
    Context mContext;

    public SubjectAdapter(List<Subject> subjectList,Context mContext) {
        this.subjectList = subjectList;
        filteredList = subjectList;
        customFilter = new CustomFilter();
        mContext=this.mContext;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_grid_layout, viewGroup, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder subjectViewHolder, int position) {

//        Subject subject = subjectList.get(position);
//        subjectViewHolder.subjectTitle.setText(subject.getTitle());

        Subject subject = filteredList.get(position);
        subjectViewHolder.subjectTitle.setText(subject.getTitle());
        subjectViewHolder.cvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PlayerActivity.class);
                intent.putExtra("Subject_ID",subjectList.get(position).getSubjectID());
                intent.putExtra("Subject_Title",subjectList.get(position).getTitle());
                view.getContext().startActivity(intent);
            }
        });

        if (subject.getTitle().toLowerCase().contains("english")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_english);
        }else if (subject.getTitle().toLowerCase().contains("prose")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_prose);
        }else if (subject.getTitle().toLowerCase().contains("poetry")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_poetry);
        }else if (subject.getTitle().toLowerCase().contains("grammar")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_grammar);
        }else if (subject.getTitle().toLowerCase().contains("social")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_social);
        }else if (subject.getTitle().toLowerCase().contains("history")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_history);
        }else if (subject.getTitle().toLowerCase().contains("civics")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_civics);
        }else if (subject.getTitle().toLowerCase().contains("economics")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_economics);
        }else if (subject.getTitle().toLowerCase().contains("geography")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_geography);
        }else if (subject.getTitle().toLowerCase().contains("science")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_science);
        }else if (subject.getTitle().toLowerCase().contains("physics")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_physics);
        }else if (subject.getTitle().toLowerCase().contains("chemistry")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_chemistry);
        }else if (subject.getTitle().toLowerCase().contains("biology")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_biology);
        }else if (subject.getTitle().toLowerCase().contains("zoology")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_zoology);
        }else if (subject.getTitle().toLowerCase().equals("maths")||subject.getTitle().toLowerCase().equals("mathematics")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_maths);
        }else if (subject.getTitle().toLowerCase().contains("health")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_healthedu);
        }else if (subject.getTitle().toLowerCase().contains("computer")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_computer);
        }else if (subject.getTitle().toLowerCase().contains("environment") || subject.getTitle().toLowerCase().contains("evs")){
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_evs);
        }else {
            subjectViewHolder.subjectImage.setImageResource(R.drawable.ic_book);
        }

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder{

        TextView subjectTitle;
        ImageView subjectImage;
        CardView cvSubject;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitle = itemView.findViewById(R.id.name1);
            subjectImage = itemView.findViewById(R.id.image1);
            cvSubject=itemView.findViewById(R.id.cv_subject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
    }

    public class CustomFilter extends Filter{


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();

            List<Subject> newList = subjectList;
            List<Subject> resultList = new ArrayList<>();

            String searchValue = constraint.toString().toLowerCase();

            for(int i=0;i<newList.size();i++){

                Subject subject = newList.get(i);
                String title = String.valueOf(subject.getTitle());

                if(title.toLowerCase().contains(searchValue)){
                    Log.d("TAG", "title from Object: "+title+" Search value:"+searchValue+" and Subject Title:"+subject.getTitle());
                    resultList.add(subject);
                }

            }


            filterResults.count = resultList.size();
            filterResults.values = resultList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredList = (List<Subject>) results.values;
            notifyDataSetChanged();

        }
    }
}
