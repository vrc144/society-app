package com.example.societyapp.ui2.home2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.societyapp.R;
import com.example.societyapp.Visitor;
import com.example.societyapp.ui.home.NewVisitorAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GuardHistoryAdapter extends RecyclerView.Adapter<GuardHistoryAdapter.VisitorViewHolder> {

    Context ctx;
    List<Visitor> visitorList;

    public GuardHistoryAdapter(Context ctx, List<Visitor> visitorList) {
        this.ctx = ctx;
        this.visitorList = visitorList;
    }

    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_guard_visitor, null);
        return new GuardHistoryAdapter.VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        final Visitor visitor = visitorList.get(position);
        if(visitor.getImage().equals("")){
            Log.i("hi","hi");
            holder.image.setImageResource(R.drawable.profile_picture);
        }
        else {
            Picasso
                    .get()
                    .load(visitor.getImage().toString())
                    .into(holder.image);
        }
        if (visitor.getStatus().equals("accepted")){
            holder.cardGuard.setCardBackgroundColor(ctx.getResources().getColor(R.color.lightSuccess,null));
        }
        else{
            holder.cardGuard.setCardBackgroundColor(ctx.getResources().getColor(R.color.lightDanger,null));
        }
        holder.name.setText(visitor.getName());
        holder.contactNo.setText(visitor.getContactNumber());
        holder.vehicleNo.setText(visitor.getVehicleNumber());
        holder.rov.setText("Reason: " + visitor.getReasonOfVisit());
        holder.date.setText(visitor.getDate());
        holder.time.setText(", " + visitor.getTime());
        holder.building.setText(visitor.getBuilding());
        holder.floor.setText(", " + visitor.getFloor());
        holder.flat.setText(", " + visitor.getFlat());
    }

    @Override
    public int getItemCount() {
        return visitorList.size();
    }

    public class VisitorViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, contactNo, vehicleNo, rov, date, time, building, floor, flat;
        CardView cardGuard;

        public VisitorViewHolder(@NonNull View itemView) {
            super(itemView);

            cardGuard = itemView.findViewById(R.id.cardGuardVisitor);
            image = itemView.findViewById(R.id.imageGV);
            name = itemView.findViewById(R.id.nameGV);
            contactNo = itemView.findViewById(R.id.contactNoGV);
            vehicleNo = itemView.findViewById(R.id.vehicleNoGV);
            rov = itemView.findViewById(R.id.rovGV);
            date = itemView.findViewById(R.id.dateGV);
            time = itemView.findViewById(R.id.timeGV);
            building = itemView.findViewById(R.id.buildingGV);
            floor = itemView.findViewById(R.id.floorGV);
            flat = itemView.findViewById(R.id.flatGV);
        }
    }
}
