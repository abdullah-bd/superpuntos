package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.admin.activity.AdminReleaseActivity;
import com.iotait.superpuntos.models.ClaimedReward;

import static com.iotait.superpuntos.helper.Constants.CLAIMED_LIST;
import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class RewardAdminAdapter extends RecyclerView.Adapter<RewardAdminAdapter.RewardViewHolder> {

    private Context mContext;

    public RewardAdminAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_reward_admin, null);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, final int position) {

        ClaimedReward claimedReward = CLAIMED_LIST.get(position);

        if (CLAIMED_LIST.get(position).getUri()==null) {
            Glide.with(mContext).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }else {
            if (CLAIMED_LIST.get(position).getUri().equals("")){
                Glide.with(mContext).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
            }else {
                Glide.with(mContext).load(CLAIMED_LIST.get(position).getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
            }
        }

        holder.tvName.setText(claimedReward.getUserName());
        holder.tvDate.setText(claimedReward.getClaimingDate());
        holder.tvAmount.setText("C$"+claimedReward.getClaimedAmount());

        holder.btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AdminReleaseActivity.class).putExtra("Position", position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return CLAIMED_LIST.size();
    }

    static class RewardViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate, tvAmount;
        Button btnRelease;
        ImageView iv;
        RelativeLayout rl;

        RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            btnRelease = itemView.findViewById(R.id.btnRelease);
            iv = itemView.findViewById(R.id.ivUser);
            rl = itemView.findViewById(R.id.rleowrewardad);
        }


    }
}


