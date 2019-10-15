package com.example.societyapp.ui.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.societyapp.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

    Context ctx;
    List<Notice>noticeList;

    public NoticeAdapter(Context ctx, List<Notice> noticeList) {
        this.ctx = ctx;
        this.noticeList = noticeList;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_notice, parent,false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        final Notice notice = noticeList.get(position);
        holder.noticeTitle.setText(notice.getTitle());
        holder.noticeBody.setText(notice.getBody());
        holder.noticeDate.setText(notice.getDate());
        holder.noticeTime.setText(notice.getTime());
    }

    @Override
    public int getItemCount() {
       return noticeList.size();
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView noticeTitle,noticeBody,noticeDate,noticeTime;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            noticeTitle = itemView.findViewById(R.id.noticeTitle);
            noticeBody = itemView.findViewById(R.id.noticeBody);
            noticeDate = itemView.findViewById(R.id.noticeDate);
            noticeTime = itemView.findViewById(R.id.noticeTime);
        }
    }
}
