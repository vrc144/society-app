package com.example.societyapp.ui.notice;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.societyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NoticeBoardFragment extends Fragment {

    RecyclerView noticeBoard;
    DatabaseReference ref;
    List<Notice>noticeList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notice_board, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noticeBoard = (RecyclerView)view.findViewById(R.id.recyclerViewNoticeBoard);
        noticeBoard.setHasFixedSize(true);
        noticeBoard.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeList = new ArrayList<Notice>();
        ref = FirebaseDatabase.getInstance().getReference("notices");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds2: dataSnapshot.getChildren()){
                    HashMap<String,String>n = (HashMap<String, String>) ds2.getValue();
                    Log.i("title",n.get("title"));
                    Notice notice = new Notice(n.get("title"),n.get("body"),n.get("date"),n.get("time"));
                    noticeList.add(notice);
                }
                Collections.reverse(noticeList);
                NoticeAdapter adapter = new NoticeAdapter(getContext(),noticeList);
                noticeBoard.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}