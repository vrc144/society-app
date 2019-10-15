package com.example.societyapp.ui2.addVisitor;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.societyapp.R;
import com.example.societyapp.Resident;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SelectFlatDialog extends DialogFragment {

    DatabaseReference ref,ref2,ref3;
    ListView listBuildings,listFloors,listFlats;
    TextView textViewList;
    ArrayAdapter<String>adapterBuildings,adapterFloors,adapterFlats;
    String[] arrayOfBuildings,arrayOfFloors,arrayOfFlats;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View content = inflater.inflate(R.layout.fragment_select_flat, null);
        builder.setView(content)
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        listBuildings = content.findViewById(R.id.listBuildings);
        listFloors = content.findViewById(R.id.listFloors);
        listFlats = content.findViewById(R.id.listFlats);
        textViewList = content.findViewById(R.id.textViewList);

        listFloors.setVisibility(View.INVISIBLE);
        listFlats.setVisibility(View.INVISIBLE);

        textViewList.setText("Select the building name");
        ref = FirebaseDatabase.getInstance().getReference("structure");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object>structure = (HashMap<String, Object>) dataSnapshot.getValue();
                Set<String>buildings = structure.keySet();
                arrayOfBuildings = buildings.toArray(new String[0]);
                adapterBuildings = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayOfBuildings);
                listBuildings.setAdapter(adapterBuildings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textViewList.setText("Select the floor number");
                String building = arrayOfBuildings[position];
                Log.i("building",building);
                ref2 = FirebaseDatabase.getInstance().getReference("structure").child(building);
                listBuildings.setVisibility(View.INVISIBLE);
                listFloors.setVisibility(View.VISIBLE);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("datasnapshot",dataSnapshot.getValue().toString());
                        Log.i("key",dataSnapshot.getKey());

                        HashMap<String,Object>structure = (HashMap<String, Object>) dataSnapshot.getValue();
                        Set<String>floors = structure.keySet();
                        arrayOfFloors = floors.toArray(new String[0]);
                        adapterFloors = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayOfFloors);
                        listFloors.setAdapter(adapterFloors);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        listFloors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textViewList.setText("Select the flat number and press submit");
                String floor = arrayOfFloors[position];
                Log.i("floor",floor);
                ref3 = ref2.child(floor);
                listFlats.setVisibility(View.VISIBLE);
                listFloors.setVisibility(View.INVISIBLE);
                ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("datasnapshot",dataSnapshot.getValue().toString());
                        HashMap<String,Object>f = (HashMap<String, Object>) dataSnapshot.getValue();
                        Set<String>flats = f.keySet();
                        arrayOfFlats = flats.toArray(new String[0]);
                        adapterFlats = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayOfFlats);
                        listFlats.setAdapter(adapterFlats);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return builder.create();
    }

}
