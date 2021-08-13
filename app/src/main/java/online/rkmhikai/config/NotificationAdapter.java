package online.rkmhikai.config;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

import online.rkmhikai.R;
import online.rkmhikai.ui.notification.NoticeActivity;
import online.rkmhikai.Tables.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<Notification> mData;
    RequestOptions option;

    public NotificationAdapter(Context mContext, List<Notification> mData) {
        this.mContext = mContext;
        this.mData = mData;

        //Request option for glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.notification_row_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, NoticeActivity.class);
                i.putExtra("notice_title",mData.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("notice_text",mData.get(viewHolder.getAdapterPosition()).getText());
                i.putExtra("notice_img",mData.get(viewHolder.getAdapterPosition()).getImgNotice());
                mContext.startActivity(i);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNtitle.setText(mData.get(position).getTitle());
        String test=Html.fromHtml(mData.get(position).getText()).toString();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
////            holder.tvNtext.setText(Html.fromHtml(mData.get(position).getText(),Html.FROM_HTML_MODE_LEGACY));
//            holder.tvNtext.setText(test);
//        } else {
////            holder.tvNtext.setText(Html.fromHtml(mData.get(position).getText()));
//            holder.tvNtext.setText(test);
//        }

        holder.tvNtext.setText(test);

//        holder.tvNtext.setText(mData.get(position).getText());

        //Load image from the internet and set it into ImageView using glide

        //Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);
        Glide.with(mContext).load("https://rkmhikai.online/public/assets/img/swamiji.png").apply(option).into(holder.ivThumbnail);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNtitle;
        TextView tvNtext;
        ImageView ivThumbnail;

        LinearLayout view_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tvNtitle = itemView.findViewById(R.id.n_title);
            tvNtext = itemView.findViewById(R.id.n_text);
            ivThumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }




}
