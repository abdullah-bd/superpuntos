package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.LoginActivity;
import com.iotait.superpuntos.activity.ProfileActivity;
import com.iotait.superpuntos.activity.SurveyActivity;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.LogoutDialog;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.Survey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.IS_ADMIN;
import static com.iotait.superpuntos.helper.Constants.PARTIAL_INFO;
import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST;
import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST_FINAL;
import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;


public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>  implements LogoutDialog.OnYesButtonClick, LogoutDialog.OnNoButtonClick, PopUpDialog.OnOkButtonClick{

    private Context mContext;
    private int count, progress;
    private int pos;
    private LogoutDialog dialog;
    private PopUpDialog popUpDialog;
    List<Survey> items = (Constants.USER.getName()==null)? SURVEY_LIST : SURVEY_LIST_FINAL;
    public SurveyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.row_survey, null);

        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, final int position) {
        holder.title.setText(items.get(position).getSurveyName());
        holder.subtitle.setText(items.get(position).getSurveyCategory());
        TextView points = holder.view.findViewById(R.id.tvpointstoearn);
        ImageView pointsIV = holder.view.findViewById(R.id.iv_earn);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");

        try {
            Date date1 = simpleDateFormat.parse(items.get(position).getSurveyDeadline());
            Date date2 = simpleDateFormat.parse(getTimeStamp());

            holder.daysLeft.setText(printDifference(date2, date1));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        ImageView iv=holder.view.findViewById(R.id.tv_delete);
        if (!IS_ADMIN){
            iv.setVisibility(View.GONE);
            points.setVisibility(View.VISIBLE);
            pointsIV.setVisibility(View.VISIBLE);
            points.setText("C$"+ items.get(position).getSurveyPoints()+" PUNTOS");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(items.get(position).getSurveyMaxParticipant()) <= count){
                        showDialog("Oops!", "Maximum Number of Participants have already participated in this Survey");
                    } else {
                        Intent intent = new Intent(mContext, SurveyActivity.class);
                        Constants.SURVEY = items.get(position);
                        mContext.startActivity(intent);
                    }

                }
            });

            Integer num = PARTIAL_INFO.get(items.get(position).getSurveyId());
            Log.d("amii", "onBindViewHolder: "+num+", Position-"+position);
            if (num!=null){
                if (num==items.get(position).getQuestionList().size()){
                    holder.pb.setProgress(100);
                    holder.percent.setText("Completa");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog("Completa", "Ya has completado esta encuesta.");
                        }
                    });
                }else {
                    double a= (double) num/items.get(position).getQuestionList().size()*100;
                    int b= (int)a;
                    holder.percent.setText(b+"%");
                    holder.pb.setProgress(b);

                }

            }
        } else {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mContext, items.get(position).getSurveyId(), Toast.LENGTH_SHORT).show();
                    pos=position;
                    createDialog(items.get(position).getSurveyId());

                }
            });
        }
        getParticipants(items.get(position).getSurveyId(), items.get(position).getSurveyMaxParticipant(), holder.participants, holder.pb);
    }

    private void createDialog(String key) {
        Constants.DELETE_KEY = key;
        dialog = new LogoutDialog(mContext,"Delete?","Do you really want to delete this survey?");
        dialog.setListener(this, this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getParticipants(String surveyId, final String surveyMaxParticipant, final TextView participants, final ProgressBar pb) {
        DatabaseReference participantReference = FirebaseDatabase.getInstance().getReference(Constants.TEST+"participant").child(surveyId);
        participantReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                if (IS_ADMIN){
                    participants.setText(count +" Participants");
                    progress = (count * 100) / Integer.parseInt(surveyMaxParticipant);
                    Log.e("TAG", "onDataChange: "+progress );
                    pb.setProgress(progress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onYesButtonClick() {
        Toast.makeText(mContext,"Deleted Successfully" , Toast.LENGTH_SHORT).show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST+"surveys/");
        ref.child(Constants.DELETE_KEY).removeValue();
        DatabaseReference ref2 = database.getReference(Constants.TEST+"deletedsurveys/");
        ref2.child(Constants.DELETE_KEY).setValue(items.get(pos));
        items.remove(pos);
        Constants.ADMIN_SURVEYADAPTER.notifyDataSetChanged();
        dialog.dismiss();

    }

    @Override
    public void onNoButtonClick() {
        dialog.dismiss();

    }

    @Override
    public void onOkButtonClick() {
        popUpDialog.dismiss();
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView title, subtitle,percent, daysLeft, participants;
        ProgressBar pb;
        SurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            title = itemView.findViewById(R.id.tvSurveyTitle);
            subtitle = itemView.findViewById(R.id.tvSurveySubTitle);
            percent = itemView.findViewById(R.id.tvPercentage1);
            daysLeft = itemView.findViewById(R.id.tvDaysLeft);
            participants = itemView.findViewById(R.id.tvParticipant);
            pb=itemView.findViewById(R.id.progressBar1);
        }


    }

    private String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return "Expira en "+elapsedDays +" dias";

    }

    private void showDialog(String title, String body) {
        popUpDialog = new PopUpDialog(mContext, title, body);
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }
}
