package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.admin.activity.AdminUserListActivity;

import java.util.List;

import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class UserListAdminAdapter extends RecyclerView.Adapter<UserListAdminAdapter.UserListViewHolder> {

    private Context mContext;

    public UserListAdminAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row_users_admin, null);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {

        holder.tvUserName.setText(USER_LIST.get(position).getName());
        if (USER_LIST.get(position).getUri()==null) {
            Glide.with(mContext).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }else {
            if (USER_LIST.get(position).getUri().equals("")){
                Glide.with(mContext).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
            }else {
                Glide.with(mContext).load(USER_LIST.get(position).getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
            }
        }
        holder.tvDate.setText("C$"+USER_LIST.get(position).getCoins()+" | "+USER_LIST.get(position).getCreationDate());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AdminUserListActivity.class).putExtra("Position", position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return USER_LIST.size();
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder{

        TextView tvUserName, tvDate;
        Button btnView;
        ImageView iv;

        UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnView = itemView.findViewById(R.id.btnView);
            iv = itemView.findViewById(R.id.ivUser);
        }


    }
}


