package com.example.societyapp.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.societyapp.PermissionNotification;
import com.example.societyapp.R;
import com.example.societyapp.Visitor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class NewVisitorAdapter extends RecyclerView.Adapter<NewVisitorAdapter.VisitorViewHolder> {

    Context ctx;
    List<Visitor> visitorList;

    public NewVisitorAdapter(Context ctx, List<Visitor> visitorList) {
        this.ctx = ctx;
        this.visitorList = visitorList;
    }

    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_visitor,null);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, final int position) {
        final Visitor visitor = visitorList.get(position);
        holder.nameNewVisitor.setText(visitor.getName());
        holder.contactNoNewVisitor.setText(visitor.getContactNumber());
        holder.vehicleNoNewVisitor.setText(visitor.getVehicleNumber());
        holder.rovNewVisitor.setText("Reason: "+visitor.getReasonOfVisit());
        holder.dateNewVisitor.setText(visitor.getDate());
        holder.timeNewVisitor.setText(",  "+visitor.getTime());
        if(visitor.getImage().equals("")){
            Log.i("hi","hi");
            holder.imageNewVisitor.setImageResource(R.drawable.profile_picture);
        }
        else {
            Picasso
                    .get()
                    .load(visitor.getImage().toString())
                    .into(holder.imageNewVisitor);
        }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitorList.remove(position);
                notifyDataSetChanged();
                String key = visitor.getKey();
                Log.i("keyyy",key);
                String title = "Visitor Status: "+visitor.getName();
                String body = "Accepted! Building: "+visitor.getBuilding()+", Floor: "+visitor.getFloor()+", Flat: "+visitor.getFlat();
                try {
                    PermissionNotification notification = new PermissionNotification(ctx,title,body,visitor.getGuardId());
                    notification.sendNotification();
                    Toast.makeText(ctx, "Notification sent!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String house = visitor.getBuilding()+visitor.getFloor()+visitor.getFlat();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("oldVisitors").child(house);
                HashMap<String,String>vis = new HashMap<String,String>();
                vis.put("status","accepted");
                vis.put("name",visitor.getName());
                vis.put("contactNumber",visitor.getContactNumber());
                vis.put("vehicleNumber",visitor.getVehicleNumber());
                vis.put("building",visitor.getBuilding());
                vis.put("floor",visitor.getFloor());
                vis.put("flat",visitor.getFlat());
                vis.put("reasonOfVisit",visitor.getReasonOfVisit());
                vis.put("guardId",visitor.getGuardId());
                vis.put("image",visitor.getImage());
                vis.put("date",visitor.getDate());
                vis.put("time",visitor.getTime());
                ref.push().setValue(vis);
                DatabaseReference newref = FirebaseDatabase.getInstance().getReference("newVisitors").child(house).child(visitor.getKey());
                newref.removeValue();
                Toast.makeText(ctx, "Done!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitorList.remove(position);
                notifyDataSetChanged();
                String key = visitor.getKey();
                String title = "Visitor Status: "+visitor.getName();
                String body = "Declined! Building: "+visitor.getBuilding()+", Floor: "+visitor.getFloor()+", Flat: "+visitor.getFlat();
                try {
                    PermissionNotification notification = new PermissionNotification(ctx,title,body,visitor.getGuardId());
                    notification.sendNotification();
                    Toast.makeText(ctx, "Guard will be informed!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String house = visitor.getBuilding()+visitor.getFloor()+visitor.getFlat();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("oldVisitors").child(house);
                HashMap<String,String>vis = new HashMap<String,String>();
                vis.put("status","declined");
                vis.put("name",visitor.getName());
                vis.put("contactNumber",visitor.getContactNumber());
                vis.put("vehicleNumber",visitor.getVehicleNumber());
                vis.put("building",visitor.getBuilding());
                vis.put("floor",visitor.getFloor());
                vis.put("flat",visitor.getFlat());
                vis.put("reasonOfVisit",visitor.getReasonOfVisit());
                vis.put("guardId",visitor.getGuardId());
                vis.put("image",visitor.getImage());
                vis.put("date",visitor.getDate());
                vis.put("time",visitor.getTime());
                ref.push().setValue(vis);
                DatabaseReference newref = FirebaseDatabase.getInstance().getReference("newVisitors").child(house).child(visitor.getKey());
                newref.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitorList.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder{

        ImageView imageNewVisitor;
        TextView nameNewVisitor,contactNoNewVisitor,vehicleNoNewVisitor,rovNewVisitor,dateNewVisitor,timeNewVisitor;
        MaterialButton accept, decline;

        public VisitorViewHolder(@NonNull View itemView) {
            super(itemView);

            imageNewVisitor = itemView.findViewById(R.id.imageNewVisitor);
            nameNewVisitor = itemView.findViewById(R.id.nameNewVisitor);
            contactNoNewVisitor = itemView.findViewById(R.id.contactNoNewVisitor);
            vehicleNoNewVisitor = itemView.findViewById(R.id.vehicleNoNewVisitor);
            rovNewVisitor = itemView.findViewById(R.id.rovNewVisitor);
            dateNewVisitor = itemView.findViewById(R.id.dateNewVisitor);
            timeNewVisitor = itemView.findViewById(R.id.timeNewVisitor);
            accept = itemView.findViewById(R.id.acceptButton);
            decline = itemView.findViewById(R.id.declineButton);
        }
    }

}
