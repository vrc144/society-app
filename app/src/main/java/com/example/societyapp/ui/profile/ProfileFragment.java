package com.example.societyapp.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.societyapp.NavigationActivity;
import com.example.societyapp.R;
import com.example.societyapp.Resident;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    DatabaseReference ref;
    String userId;
    TextView profileUserId,profileUserName,profileContactNo,profileEmail,profileFlat,profileBuilding,profileFloor;
    Button changeProfileButton;
    String building,name,email;
    int floor,flat;
    long contactNo;

    Resident currentResident;
    ImageView profilePicture;
    ChangeProfileDialog dialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity activity = (NavigationActivity)getActivity();
        Log.i("key",activity.getUserId());
        userId = activity.getUserId();

        profileUserId = (TextView)view.findViewById(R.id.profileUserId);
        profileUserName = (TextView)view.findViewById(R.id.profileUserName);
        profileContactNo = (TextView)view.findViewById(R.id.profileContactNo);
        profileEmail = (TextView)view.findViewById(R.id.profileEmail);
        profileBuilding = (TextView)view.findViewById(R.id.profileBuilding);
        profileFloor = (TextView)view.findViewById(R.id.profileFloor);
        profileFlat = (TextView)view.findViewById(R.id.profileFlat);
        changeProfileButton = (Button)view.findViewById(R.id.changeProfileButton);
        profilePicture = (ImageView)view.findViewById(R.id.profilePicture);

        dialog = new ChangeProfileDialog();


//        final TextView textView = root.findViewById(R.id.text_profile);
//        profileViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }

//        });

        ref = FirebaseDatabase.getInstance().getReference("residents").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> resident = (HashMap<String, Object>) dataSnapshot.getValue();
                building = resident.get("building").toString();
                floor = Integer.parseInt(resident.get("floor").toString());
                flat = Integer.parseInt(resident.get("flat").toString());
                name = resident.get("name").toString();
                contactNo = Long.parseLong(resident.get("contactNo").toString());
                email = resident.get("email").toString();
                String password = resident.get("password").toString();
                currentResident = new Resident(userId, password, building, floor, flat, name, contactNo, email);

                profileUserId.setText("#"+userId);
                profileUserName.setText("Name:" +name);
                profileEmail.setText("Email: "+email);
                profileBuilding.setText("Building: "+building);
                profileFloor.setText("Floor Number: "+String.valueOf(floor));
                profileFlat.setText("Flat: "+String.valueOf(flat));
                profileContactNo.setText("Contact Number: "+String.valueOf(contactNo));
                if(resident.get("profilePicture")!=null){
                    Picasso
                            .get()
                            .load(resident.get("profilePicture").toString())
                            .into(profilePicture);
                }
                else{
                    profilePicture.setImageResource(R.drawable.profile_picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentResident",currentResident);
                dialog.setArguments(bundle);
                dialog.show(fragmentManager,"Yes");
            }
        });


    }
}