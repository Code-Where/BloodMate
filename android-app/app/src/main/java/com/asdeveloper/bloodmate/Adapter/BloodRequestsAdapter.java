package com.asdeveloper.bloodmate.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.R;
import com.asdeveloper.bloodmate.RequestDescriptionActivity;
import com.asdeveloper.bloodmate.SharedResources;

import java.util.Calendar;
import java.util.List;

public class BloodRequestsAdapter extends RecyclerView.Adapter<BloodRequestsAdapter.ViewHolder>{
    private List<BloodRequests> requests;
    private Context context;

    public BloodRequestsAdapter(List<BloodRequests> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<BloodRequests> newList) {
        requests.clear();
        requests.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BloodRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_requests_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BloodRequestsAdapter.ViewHolder holder, int position) {
        String date = "";
        BloodRequests bloodRequests = requests.get(position);
        Calendar deadLineDate = Calendar.getInstance();
        deadLineDate.setTimeInMillis(bloodRequests.getDeadline());
        date = "Deadline : " + deadLineDate.get(Calendar.DAY_OF_MONTH) + "/" + (deadLineDate.get(Calendar.MONTH) + 1) + "/" + deadLineDate.get(Calendar.YEAR);
        holder.requsterName.setText(bloodRequests.getRequester().getName());
        holder.bloodType.setText(bloodRequests.getBloodGroup());
        holder.location.setText(bloodRequests.getCity());
        holder.requestDate.setText(date);
        holder.emergencyType.setText(bloodRequests.getUrgencyLevel());
        if (bloodRequests.getRequester().getId() == SharedResources.getId(holder.itemView.getContext())){
            holder.note.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#005900")));
            holder.note.setText("This Request is Created By You");
        }
        else
            holder.note.setText(bloodRequests.getNote());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout requestContainer;
        TextView requsterName, bloodType, location, requestDate, emergencyType, note;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requsterName = itemView.findViewById(R.id.tv_requester_name);
            bloodType = itemView.findViewById(R.id.tv_blood_type);
            location = itemView.findViewById(R.id.tv_location);
            requestDate = itemView.findViewById(R.id.tv_request_date);
            emergencyType = itemView.findViewById(R.id.tv_emergency_type);
            note = itemView.findViewById(R.id.tv_note);
            requestContainer = itemView.findViewById(R.id.requestContainer);
            requestContainer.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), RequestDescriptionActivity.class);
                intent.putExtra("requestId", requests.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
