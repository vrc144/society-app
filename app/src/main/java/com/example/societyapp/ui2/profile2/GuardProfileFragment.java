package com.example.societyapp.ui2.profile2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.societyapp.NavigationActivity;
import com.example.societyapp.NavigationActivity2;
import com.example.societyapp.R;
import com.example.societyapp.ui.profile.ChangeProfileDialog;
import com.example.societyapp.ui2.addVisitor.AddVisitorFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class GuardProfileFragment extends Fragment {

    private GuardProfileViewModel guardProfileViewModel;
    String address, name, userId;
    long contact, gateno, shift;
    TextView guardName, guardAddress, guardContact, guardShift, guardGate, guardUserId;
    ImageView profilePictureGuard;
    ChangeProfileDialog dialog;
    DatabaseReference ref;
    StorageReference mStorageRef;
    MaterialButton addProfilePictureGuard;
    Uri mImageUri;
    final private int CAMERA_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        guardProfileViewModel =
                ViewModelProviders.of(this).get(GuardProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_guardprofile, container, false);
        root = inflater.inflate(R.layout.fragment_guardprofile, container, false);
        return root;

    }

    ;


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity2 activity = (NavigationActivity2) getActivity();
        Log.i("key", activity.getUserId());


        userId = activity.getUserId();
        guardName = (TextView) view.findViewById(R.id.guardName);
        guardAddress = (TextView) view.findViewById(R.id.guardAddress);
        guardContact = (TextView) view.findViewById(R.id.guardContact);
        guardShift = (TextView) view.findViewById(R.id.guardShift);
        guardGate = (TextView) view.findViewById(R.id.guardGate);
        guardUserId = (TextView) view.findViewById(R.id.guardUserId);
        mStorageRef = FirebaseStorage.getInstance().getReference(userId);
        profilePictureGuard = view.findViewById(R.id.profilePictureGuard);
        addProfilePictureGuard = view.findViewById(R.id.addProfilePictureGuard);

        ref = FirebaseDatabase.getInstance().getReference("guards").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> guard = (HashMap<String, Object>) dataSnapshot.getValue();

                name = guard.get("name").toString();
                address = guard.get("address").toString();
                contact = Long.parseLong(guard.get("contactNo").toString());
                gateno = Long.parseLong(guard.get("gateNo").toString());
                shift = Long.parseLong(guard.get("shift").toString());

                guardUserId.setText("#" + userId);
                guardName.setText("Name: " + name);
                guardContact.setText("Contact: " + contact);
                guardAddress.setText("Address: " + address);
                guardGate.setText("Gate Number: " + gateno);
                guardShift.setText("Shift: " + shift);

                if(guard.get("profilePicture")==null){
                    profilePictureGuard.setImageResource(R.drawable.profile_picture);
                }
                else{
                    Picasso
                            .get()
                            .load(guard.get("profilePicture").toString())
                            .into(profilePictureGuard);
                }
            }
        });

        addProfilePictureGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(getContext(), GuardProfileFragment.this);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                {
                    mImageUri = result.getUri();
                    Log.i("mImageUri", mImageUri.toString());
                    profilePictureGuard.setImageURI(mImageUri);
                    if (mImageUri != null) {
                        final StorageReference fileReference = mStorageRef.child(userId + "." + getFileExtendsion(mImageUri));
                        fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("guards/" + userId).child("profilePicture");
                                        ref2.setValue(uri.toString());
                                        Log.i("Url", uri.toString());
                                        Toast.makeText(getContext(), "Profile Photo updated!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Fail", e.getMessage());
                            }
                        });
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                }
            }
        }
    }

    public String getFileExtendsion(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}