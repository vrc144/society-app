package com.example.societyapp.ui.home;

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

public class HomeFragment extends Fragment {

    String userId,topic;
    List<Visitor>newVisitors;
    DatabaseReference ref,ref2;
    RecyclerView recyclerView;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity activity = (NavigationActivity)getActivity();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = activity.getUserId();
        newVisitors = new ArrayList<Visitor>();
        ref = FirebaseDatabase.getInstance().getReference("residents").child(userId);
        topic = "";
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String building = dataSnapshot.child("building").getValue().toString();
                String floor = dataSnapshot.child("floor").getValue().toString();
                String flat = dataSnapshot.child("flat").getValue().toString();
                topic = building + floor + flat;
//                Log.i("flatnumber",topic);
                Log.i("topic in home fragment",topic);
                ref2 = FirebaseDatabase.getInstance().getReference("newVisitors").child(topic);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        newVisitors.clear();
                        Log.i("key",dataSnapshot.getKey());
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Log.i("key10",snapshot.getKey());
                            HashMap<String,String>newVisitor = (HashMap<String, String>) snapshot.getValue();
                            String guardId = newVisitor.get("guardId").toString();
                            String name = newVisitor.get("name").toString();
                            String cn = newVisitor.get("contactNumber").toString();
                            String vn = newVisitor.get("vehicleNumber").toString();
                            String rov = newVisitor.get("reasonOfVisit").toString();
                            String building = newVisitor.get("building").toString();
                            String floor = newVisitor.get("floor").toString();
                            String flat = newVisitor.get("flat").toString();
                            String image = "";
                            Log.i("key6",snapshot.getKey());
                            if(snapshot.child("image").exists()){
                                image = newVisitor.get("image").toString();
                            }
                            Visitor vis = new Visitor(snapshot.getKey(),name,cn,vn,rov,image,building,floor,flat,guardId,newVisitor.get("date"),newVisitor.get("time"),"");
                            newVisitors.add(vis);
                            Collections.reverse(newVisitors);
                        }
                        NewVisitorAdapter adapter = new NewVisitorAdapter(getContext(),newVisitors);
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