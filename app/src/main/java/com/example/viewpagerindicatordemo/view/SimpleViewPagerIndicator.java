package com.example.viewpagerindicatordemo.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viewpagerindicatordemo.R;

/**
 * Created by Administrator on 2017/9/19/019.
 */

public class SimpleViewPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout linearLayout;

    public SimpleViewPagerIndicator(Context context) {
        super(context);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //设置相关联的viewpager
    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(this);



        //定义限行布局 添加到simpleviewpagerindicator
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(linearLayout);
        for (int i = 0; i <viewPager.getAdapter().getCount() ; i++) {
            View view = View.inflate(getContext(), R.layout.simple_indicator_item, null);
            view.setOnClickListener(this);
            TextView titleText = view.findViewById(R.id.item_text);
            TextView indicator = view.findViewById(R.id.item_indicator);
            if(i==0){
                indicator.setVisibility(VISIBLE);
                titleText.setTextColor(Color.RED);
            }
            titleText.setTextSize(20);
            titleText.setText(viewPager.getAdapter().getPageTitle(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,5,10,5);
            linearLayout.addView(view,params);
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //获取所有标题 并遍历
        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            TextView title = view.findViewById(R.id.item_text);
            View indicator = view.findViewById(R.id.item_indicator);
            if(i == position){

                //让当前指示器跟着选中的控件移动
                this.scrollTo((int) linearLayout.getChildAt(i).getX()+linearLayout.getChildAt(i).getMeasuredWidth()/2
                        - getMeasuredWidth()/2, 0);
                title.setTextColor(Color.RED);
                indicator.setVisibility(VISIBLE);
            }else {
                title.setTextColor(Color.BLACK);
                indicator.setVisibility(INVISIBLE);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        //获取所有标题 并遍历
        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            //判断当前点中的是哪个view
            if(view == linearLayout.getChildAt(i)){
                viewPager.setCurrentItem(i);
            }

        }
    }
}
