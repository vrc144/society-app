package com.example.societyapp.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.societyapp.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    Context ctx;
    List<Contact>contacts;

    public ContactsAdapter(Context ctx, List<Contact> contacts) {
        this.ctx = ctx;
        this.contacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_contact, parent,false );
        return new ContactsAdapter.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final Contact contact = contacts.get(position);
        holder.name.setText(contact.getName());
        holder.job.setText(contact.getJob());
        holder.contactNumber.setText(contact.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView name,job,contactNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contactName);
            job = itemView.findViewById(R.id.contactJob);
            contactNumber = itemView.findViewById(R.id.contactContactNo);
        }
    }
}
