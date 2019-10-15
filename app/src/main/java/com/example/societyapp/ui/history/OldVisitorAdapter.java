package com.example.societyapp.ui.history;

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

import com.example.societyapp.OldVisitor;
import com.example.societyapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OldVisitorAdapter extends RecyclerView.Adapter<OldVisitorAdapter.OldVisitorViewHolder> {

    Context ctx;
    List<OldVisitor> visitorList;

    public OldVisitorAdapter(Context ctx, List<OldVisitor> visitorList) {
        this.ctx = ctx;
        this.visitorList = visitorList;
    }

    @Override
    public OldVisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_old_visitor,null);
        return new OldVisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OldVisitorViewHolder holder, int position) {
        final OldVisitor visitor = visitorList.get(position);
        holder.nameOldVisitor.setText(visitor.getName());
        holder.contactNoOldVisitor.setText(visitor.getContactNumber());
        holder.vehicleNoOldVisitor.setText(visitor.getVehicleNumber());
        holder.rovOldVisitor.setText(visitor.getReasonOfVisit());
        holder.dateOldVisitor.setText(visitor.getDate());
        holder.timeOldVisitor.setText(",    "+visitor.getTime());
        if (visitor.getStatus().equals("accepted")){
            holder.cardOldVisitor.setCardBackgroundColor(ctx.getResources().getColor(R.color.lightSuccess,null));
        }
        else{
            holder.cardOldVisitor.setCardBackgroundColor(ctx.getResources().getColor(R.color.lightDanger,null));
        }
        if(visitor.getImage().equals("")){
            holder.imageOldVisitor.setImageResource(R.drawable.profile_picture);
        }
        else {
            Picasso
                    .get()
                    .load(visitor.getImage().toString())
                    .into(holder.imageOldVisitor);
        }
    }

    @Override
    public int getItemCount() {
        return visitorList.size();
    }

    class OldVisitorViewHolder extends RecyclerView.ViewHolder {

        ImageView imageOldVisitor;
        TextView nameOldVisitor,contactNoOldVisitor,vehicleNoOldVisitor,rovOldVisitor,dateOldVisitor,timeOldVisitor;
        CardView cardOldVisitor;

        public OldVisitorViewHolder(@NonNull View itemView) {
            super(itemView);

            cardOldVisitor = itemView.findViewById(R.id.cardOldVisitor);
            imageOldVisitor = itemView.findViewById(R.id.imageOldVisitor);
            nameOldVisitor = itemView.findViewById(R.id.nameOldVisitor);
            contactNoOldVisitor = itemView.findViewById(R.id.contactNoOldVisitor);
            vehicleNoOldVisitor = itemView.findViewById(R.id.vehicleNoOldVisitor);
            rovOldVisitor = itemView.findViewById(R.id.rovOldVisitor);
            dateOldVisitor = itemView.findViewById(R.id.dateOldVisitor);
            timeOldVisitor = itemView.findViewById(R.id.timeOldVisitor);
        }
    }
}
