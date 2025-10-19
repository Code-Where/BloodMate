package com.asdeveloper.bloodmate.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asdeveloper.bloodmate.Models.Camps;
import com.asdeveloper.bloodmate.R;

import java.util.Calendar;
import java.util.List;

public class CampsAdapter extends RecyclerView.Adapter<CampsAdapter.ViewHolder> {
    private List<Camps> camps;

    public CampsAdapter(List<Camps> camps) {
        this.camps = camps;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Camps> newList) {
        camps.clear();
        camps.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CampsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camps_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CampsAdapter.ViewHolder holder, int position) {
        holder.campTitle.setText(camps.get(position).getTitle());
        holder.organizer.setText("Organized by " + camps.get(position).getOrganizerBy());
        holder.location.setText(camps.get(position).getLocation() + ", " + camps.get(position).getCity());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(camps.get(position).getDate());
        holder.date.setText("Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
    }

    @Override
    public int getItemCount() {
        return camps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView campTitle, organizer, location, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campTitle = itemView.findViewById(R.id.campTitle);
            organizer = itemView.findViewById(R.id.organizerText);
            location = itemView.findViewById(R.id.campLocation);
            date = itemView.findViewById(R.id.campDate);
        }
    }
}
