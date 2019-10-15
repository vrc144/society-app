package com.example.societyapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class NavigationActivity2 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String userId;
    View header;
    TextView nameTextView,userIdTextView;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.i("guard id",userId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = findViewById(R.id.nav_view2);

        header = (View)navigationView.getHeaderView(0);
        nameTextView = (TextView)header.findViewById(R.id.nameTextView2);
        userIdTextView = (TextView)header.findViewById(R.id.userIdTextView2);
        profileImageView = (ImageView)header.findViewById(R.id.profileImageView2);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("guards").child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> guard = (HashMap<String, Object>) dataSnapshot.getValue();
                nameTextView.setText(guard.get("name").toString());
                if(guard.get("profilePicture")!=null){
                    Picasso
                            .get()
                            .load(guard.get("profilePicture").toString())
                            .into(profileImageView);
                }
                else{
                    profileImageView.setImageResource(R.drawable.profile_picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileImageView.setImageResource(R.drawable.profile_picture);
        userIdTextView.setText("#"+userId);


        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_home2,R.id.nav_profile2,R.id.nav_add_visitor)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getUserId()
    {
        return userId;
    }
}
