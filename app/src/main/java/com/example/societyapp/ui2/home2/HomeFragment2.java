package com.example.societyapp.ui2.home2;

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

import com.example.societyapp.NavigationActivity2;
import com.example.societyapp.OldVisitor;
import com.example.societyapp.R;
import com.example.societyapp.Visitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment2 extends Fragment {

    RecyclerView guardHomeView;
    DatabaseReference ref;
    String userId;
    List<Visitor>visitors;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home2, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationActivity2 activity2 = (NavigationActivity2)getActivity();
        userId = activity2.getUserId();

        visitors = new ArrayList<Visitor>();
        guardHomeView = (RecyclerView)view.findViewById(R.id.guardHomeView);
        guardHomeView.setHasFixedSize(true);
        guardHomeView.setLayoutManager(new LinearLayoutManager(activity2));
        ref = FirebaseDatabase.getInstance().getReference("oldVisitors");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitors.clear();
                for(DataSnapshot snapshot1: dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot2: snapshot1.getChildren()){
                        HashMap<String,String>visitor = (HashMap<String, String>) snapshot2.getValue();
                        if (visitor.get("guardId").equals(userId)){
                            String key = "";
                            String name = visitor.get("name");
                            String cn = visitor.get("contactNumber");
                            String vn = visitor.get("vehicleNumber");
                            String rov = visitor.get("reasonofVisit");
                            String image = visitor.get("image");
                            String building = visitor.get("building");
                            String floor = visitor.get("floor");
                            String flat = visitor.get("flat");
                            String guardId = visitor.get("guardId");
                            String date = visitor.get("date");
                            String time = visitor.get("time");
                            String status = visitor.get("status");
                            Visitor vis = new Visitor(key,name,cn,vn,rov,image,building,floor,flat,guardId,date,time,status);
                            visitors.add(vis);
                        }
                    }
                }
                GuardHistoryAdapter adapter = new GuardHistoryAdapter(getContext(),visitors);
                guardHomeView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}