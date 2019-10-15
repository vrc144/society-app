package com.example.societyapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.societyapp.R;
import com.example.societyapp.Resident;
import com.example.societyapp.ui2.profile2.GuardProfileFragment;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ChangeProfileDialog extends DialogFragment {

    EditText newUserName,newEmail,newContactNo;
    Resident currentResident;
    String userId;
    DatabaseReference ref;
    com.google.android.material.button.MaterialButton chooseImageButton;
    Uri mImageUri;
    ImageView newProfileImage;
    Button button2;
    StorageReference mStorageRef;
    DatabaseReference mDbRef;

    private static  final int PICK_IMAGE_REQUEST = 1;
    private static  final int RESULT_OK = -1;
    final private int CAMERA_REQUEST_CODE = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        currentResident = (Resident)getArguments().getSerializable("currentResident");
        userId = currentResident.getUserId();

        mStorageRef = FirebaseStorage.getInstance().getReference(userId);


        View content = inflater.inflate(R.layout.change_profile, null);
        builder.setView(content)
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ref = FirebaseDatabase.getInstance().getReference("residents").child(userId);
                        HashMap<String,String>newValues = new HashMap<String, String>();
                        newValues.put("name",newUserName.getText().toString());
                        newValues.put("email",newEmail.getText().toString());
                        newValues.put("contactNo",newContactNo.getText().toString());
                        ref.updateChildren((Map)newValues,null);


                        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        newUserName = (EditText)content.findViewById(R.id.newUserName);
        newEmail = (EditText)content.findViewById(R.id.newEmail);
        newContactNo = (EditText)content.findViewById(R.id.newContactNo);
        chooseImageButton = (com.google.android.material.button.MaterialButton) content.findViewById(R.id.chooseImageButton);
        newProfileImage = (ImageView)content.findViewById(R.id.newProfileImage);

        ref = FirebaseDatabase.getInstance().getReference("residents/"+userId).child("profilePicture");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Picasso
                            .get()
                            .load(dataSnapshot.getValue().toString())
                            .into(newProfileImage);
                }
                else {
                    newProfileImage.setImageResource(R.drawable.profile_picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        newUserName.setText(currentResident.getName());
        newEmail.setText(currentResident.getEmail());
        newContactNo.setText(String.valueOf(currentResident.getContactNo()));




        return builder.create();
    }



    public void openFileChooser(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(getContext(), ChangeProfileDialog.this);
            }
        }
    }

    public String getFileExtendsion(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
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
                    newProfileImage.setImageURI(mImageUri);
                    if(mImageUri!=null){
                        final StorageReference fileReference = mStorageRef.child("ProfilePicture"+"."+getFileExtendsion(mImageUri));
                        fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mDbRef = FirebaseDatabase.getInstance().getReference("residents/"+userId).child("profilePicture");
                                        mDbRef.setValue(uri.toString());
                                        Log.i("Url",uri.toString());
                                        Toast.makeText(getContext(), "Profile Picture updated!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Fail",e.getMessage());
                            }
                        });
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                }
            }
        }
    }

}
