package com.example.societyapp.ui2.addVisitor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.societyapp.NavigationActivity;
import com.example.societyapp.NavigationActivity2;
import com.example.societyapp.R;
import com.example.societyapp.VisitorNotification;
import com.example.societyapp.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AddVisitorFragment extends Fragment {

    private AddVisitorViewModel addVisitorViewModel;
    TextInputEditText visitor_name,visitor_contact_no,visitor_vehicle_no,reason_of_visit,building_name,floor_no,flat_no;
    ImageView visitor_image;
    MaterialButton click_visitor_picture,button_select_flat,button_add_visitor;
    SelectFlatDialog dialog;
    DatabaseReference ref,ref2;
    String userId,topic,key;
    StorageReference mStorageRef;
    int requestCode = 1;
    Uri mainImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addVisitorViewModel =
                ViewModelProviders.of(this).get(AddVisitorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_visitor, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        visitor_name = (TextInputEditText)view.findViewById(R.id.visitor_name);
        visitor_contact_no = (TextInputEditText)view.findViewById(R.id.visitor_contact_no);
        visitor_vehicle_no = (TextInputEditText)view.findViewById(R.id.visitor_vehicle_no);
        reason_of_visit = (TextInputEditText)view.findViewById(R.id.reason_of_visit);
        visitor_image = (ImageView)view.findViewById(R.id.visitor_image);
        click_visitor_picture = (MaterialButton)view.findViewById(R.id.click_visitor_picture);
        building_name = (TextInputEditText)view.findViewById(R.id.building_name);
        floor_no = (TextInputEditText)view.findViewById(R.id.floor_number);
        flat_no = (TextInputEditText)view.findViewById(R.id.flat_number);
        button_add_visitor = view.findViewById(R.id.button_add_visitor);

//        button_select_flat = (MaterialButton)view.findViewById(R.id.button_select_flat);
//        dialog = new SelectFlatDialog();

        click_visitor_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(getContext(),AddVisitorFragment.this);
                    }
                }
        }});

        button_add_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                String building = building_name.getText().toString();
                String floor = floor_no.getText().toString();
                String flat = flat_no.getText().toString();
                topic = building+floor+flat;
                String visitorName = visitor_name.getText().toString();
                NavigationActivity2 activity = (NavigationActivity2)getActivity();
                userId = activity.getUserId();

                Log.i("addvisitor","buttonClicked");

                mStorageRef = FirebaseStorage.getInstance().getReference(topic);

                final HashMap<String,String>vis = new HashMap<String,String>();
                ref = FirebaseDatabase.getInstance().getReference("newVisitors").child(topic);
                key = ref.push().getKey();
                ref = ref.child(key);
                vis.put("name",visitorName);
                vis.put("contactNumber",visitor_contact_no.getText().toString());
                vis.put("vehicleNumber",visitor_vehicle_no.getText().toString());
                vis.put("building",building);
                vis.put("floor",floor);
                vis.put("flat",flat);
                vis.put("reasonOfVisit",reason_of_visit.getText().toString());
                vis.put("guardId",userId);
                Log.i("key",key);
                if(mainImageUri!=null){
                    final StorageReference fileReference = mStorageRef.child(key+"."+getFileExtendsion(mainImageUri));
                    fileReference.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    vis.put("image",uri.toString());
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                                    String date = df1.format(c.getTime());
                                    SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                                    String time = df2.format(c.getTime());
                                    vis.put("date",date);
                                    vis.put("time",time);
//                                    String date =
                                    ref.setValue(vis);
                                    Toast.makeText(getContext(), "Visitor added!", Toast.LENGTH_SHORT).show();

                                    Log.i("addvisitor","buttonClicked");
                                    Log.i("Url",uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Fail",e.getMessage());
                        }
                    });
                }
                else {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                    String date = df1.format(c.getTime());
                    SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                    String time = df2.format(c.getTime());
                    vis.put("date",date);
                    vis.put("time",time);
                    Log.i("blah","blah");
                    ref.setValue(vis);
                    Toast.makeText(activity, "Visitor added!", Toast.LENGTH_SHORT).show();
                }

                try {
                    VisitorNotification notification = new VisitorNotification(getContext(),"A new visitor!","Visitor Name: "+visitorName,topic);
                    notification.sendNotification();
                    Toast.makeText(getContext(), "Notification Sent!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        button_select_flat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
//                FragmentManager manager = getFragmentManager();
//                dialog.show(manager,"Noo");
//            }
//        });

    }

    public String getFileExtendsion(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFile(){
        if(mainImageUri!=null){
            final StorageReference fileReference = mStorageRef.child(key+"."+getFileExtendsion(mainImageUri));
            fileReference.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ref2 = FirebaseDatabase.getInstance().getReference("residents/"+userId).child("profilePicture");
                            ref2.setValue(uri.toString());
                            Log.i("Url",uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Fail",e.getMessage());
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                {
                    mainImageUri = result.getUri();
                    Log.i("mainImageUri",mainImageUri.toString());
                    visitor_image.setImageURI(mainImageUri);

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}