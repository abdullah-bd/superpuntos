package com.iotait.superpuntos.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.iotait.superpuntos.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    public SliderAdapter(Context context){
        this.context = context;
    }
    private int[] sliderImageId = new int[]{
            R.drawable.ic_into_page1, R.drawable.ic_intro_page2,
    };
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((CardView) object);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        ImageView imageView = new ImageView(mContext);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(sliderImageId[position]);
//        ((ViewPager) container).addView(imageView, 0);
//        return imageView;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager) container).removeView((ImageView) object);
//    }
}
