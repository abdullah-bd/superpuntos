package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.SurveyActivity;
import com.iotait.superpuntos.models.UserActivity;

import java.util.List;


public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder> {

    private Context mContext;
    private List<UserActivity> list;

    public ActivitiesAdapter(Context mContext, List<UserActivity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_user_activities, null);
        return new ActivitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewHolder holder, final int position) {

        UserActivity act = list.get(position);
        holder.title.setText(act.getSurveyName());
        holder.date.setText(act.getDate());
        holder.earning.setText("C$"+act.getEarning());
        holder.slNo.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ActivitiesViewHolder extends RecyclerView.ViewHolder{

        TextView slNo, title, date, earning;

        ActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.tvSerial);
            title = itemView.findViewById(R.id.tvResearchName);
            date = itemView.findViewById(R.id.tvDate);
            earning = itemView.findViewById(R.id.tvEarning);
        }


    }
}

