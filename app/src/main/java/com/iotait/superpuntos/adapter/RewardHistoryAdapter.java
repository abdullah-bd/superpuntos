package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.models.ClaimedReward;
import com.iotait.superpuntos.models.UserActivity;

import java.util.List;

public class RewardHistoryAdapter extends RecyclerView.Adapter<RewardHistoryAdapter.RewardHistoryViewHolder> {

    private Context mContext;
    private List<ClaimedReward> list;

    public RewardHistoryAdapter(Context mContext, List<ClaimedReward> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RewardHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_claiming_history, null);
        return new RewardHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardHistoryViewHolder holder, final int position) {

        if (!list.get(position).isProcessing()){

            if (list.get(position).isValidnumber()){
                holder.process.setText("Enviada");
            }else {
                holder.process.setText("Num. Invalido");
            }
        }
        ClaimedReward reward = list.get(position);
        holder.date.setText(reward.getClaimingDate());
        holder.earning.setText("C$"+reward.getClaimedAmount());
        holder.slNo.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class RewardHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView slNo, date, earning,process;

        RewardHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.tvSerial);
            date = itemView.findViewById(R.id.tvDate);
            earning = itemView.findViewById(R.id.tvEarning);
            process = itemView.findViewById(R.id.tvprocessing);
        }


    }
}

