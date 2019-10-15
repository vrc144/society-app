package com.example.societyapp.ui.history;

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

import com.example.societyapp.NavigationActivity;
import com.example.societyapp.OldVisitor;
import com.example.societyapp.R;
import com.example.societyapp.Visitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    String userId,topic;
    List<OldVisitor> oldVisitors;
    DatabaseReference ref,ref2;
    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity activity = (NavigationActivity)getActivity();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = activity.getUserId();
        oldVisitors = new ArrayList<OldVisitor>();
        Log.i("userId2",userId);
        ref = FirebaseDatabase.getInstance().getReference("residents").child(userId);
        topic = "";
        //extracting the topic
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("ds",dataSnapshot.getKey());
                String building = dataSnapshot.child("building").getValue().toString();
                String floor = dataSnapshot.child("floor").getValue().toString();
                String flat = dataSnapshot.child("flat").getValue().toString();
                topic = building + floor + flat;
                Log.i("a",topic);
                Log.i("topic in history",topic);
                ref2 = FirebaseDatabase.getInstance().getReference("oldVisitors").child(topic);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("key-1",dataSnapshot.getKey());
                        oldVisitors.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Log.i("key0",snapshot.getKey());
                            Log.i("key in history",snapshot.getKey());
                            HashMap<String,String> oldVisitor = (HashMap<String, String>) snapshot.getValue();
                            String name = oldVisitor.get("name").toString();
                            String cn = oldVisitor.get("contactNumber").toString();
                            String vn = oldVisitor.get("vehicleNumber").toString();
                            String rov = oldVisitor.get("reasonOfVisit").toString();
                            String date = oldVisitor.get("date").toString();
                            String time = oldVisitor.get("time").toString();
                            String image = "";
                            if(snapshot .child("image").exists()){
                                image = oldVisitor.get("image").toString();
                            }
                            OldVisitor vis = new OldVisitor(name,cn,vn,rov,image,oldVisitor.get("guardId"),oldVisitor.get("status"),date,time);
                            oldVisitors.add(vis);
                            Collections.reverse(oldVisitors);
                        }
                        OldVisitorAdapter adapter = new OldVisitorAdapter(getContext(),oldVisitors);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}