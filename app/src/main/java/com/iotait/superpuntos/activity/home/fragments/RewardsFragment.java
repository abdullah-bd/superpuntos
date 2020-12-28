package com.iotait.superpuntos.activity.home.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotait.superpuntos.activity.RewardHistoryActivity;
import com.iotait.superpuntos.activity.cashout.CashOutActivity;
import com.iotait.superpuntos.databinding.FragmentRewardsBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.PopUpDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.HAS_CLAIMED;
import static com.iotait.superpuntos.helper.Constants.USER;

public class RewardsFragment extends Fragment implements PopUpDialog.OnOkButtonClick{

    private GestureDetector gestureDetector;

    public RewardsFragment() {
        // Required empty public constructor
    }

    private static FragmentRewardsBinding binding;
    PopUpDialog popUpDialog;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRewardsBinding.inflate(inflater, container, false);

        //gestureDetector = new GestureDetector(getContext(), this);


        populateView();

        setClickListener();
        return binding.getRoot();
    }


    private void populateView() {
        binding.tvCoin.setText("C$"+Constants.USER.getCoins());
        //binding.tvClaim.setText("(Unclaimed)");
    }
    public static void setcoinstext(){
        binding.tvCoin.setText("C$"+Constants.USER.getCoins());
    }



    @SuppressLint("ClickableViewAccessibility")
    private void setClickListener() {
        binding.btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.USER.getCoins() >= 170){
                    startActivity(new Intent(getContext(), CashOutActivity.class));
//                    int now=Constants.USER.getCoins()-170;
////                    binding.tvCoin.setText("C$"+now);
                } else {
                    showDialog("¡Oops!", "Necesitas acumular almenos C$170 para solicitar tu recarga.");
                }

            }
        });

        binding.ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RewardHistoryActivity.class));
            }
        });
        binding.imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.informationlayout.setVisibility(View.VISIBLE);
            }
        });
        binding.informationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.relativeLayout2.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                //Toast.makeText(getContext(), "Swiped top", Toast.LENGTH_SHORT).show();
                binding.informationlayout.setVisibility(View.GONE);
            }

            public void onSwipeRight() {
                //Toast.makeText(getContext(), "Swiped right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                //Toast.makeText(getContext(), "Swiped left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                //Toast.makeText(getContext(), "Swiped bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (HAS_CLAIMED){
            binding.tvClaim.setText("(Claimed)");
            binding.btnClaim.setVisibility(View.INVISIBLE);
        }
    }

    private void showDialog(String title, String body) {
        popUpDialog = new PopUpDialog(getContext(), title, body);
        popUpDialog.setCancelable(false);
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    @Override
    public void onOkButtonClick() {
        popUpDialog.dismiss();
    }


    class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 300;
            private static final int SWIPE_VELOCITY_THRESHOLD = 300;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.i("TAG", "onSingleTapConfirmed:");
                //Toast.makeText(getContext(), "Single Tap Detected", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.i("TAG", "onLongPress:");
                //Toast.makeText(getContext(), "Long Press Detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //Toast.makeText(getContext(), "Double Tap Detected", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }
}
