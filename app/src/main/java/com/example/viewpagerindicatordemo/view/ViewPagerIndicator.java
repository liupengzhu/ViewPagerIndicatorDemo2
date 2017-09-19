package com.example.viewpagerindicatordemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viewpagerindicatordemo.R;

/**
 * Created by Administrator on 2017/9/19/019.
 */

public class ViewPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private int startX;
    private int textWidth;
    private Paint paint;
    private int leftMargin = 10;
    private int currentPosition;
    private int rightMargin = 10;

    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }



    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

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
            View view = View.inflate(getContext(), R.layout.indicator_item, null);
            view.setOnClickListener(this);
            final TextView titleText = view.findViewById(R.id.indicator_item_text);
            if(i==0){
                titleText.setTextColor(Color.RED);
                //等到界面渲染出之后获取textview的宽
                titleText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        textWidth = titleText.getWidth();
                        startX = leftMargin;
                        postInvalidate();
                        //移除监听
                        titleText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

            }
            titleText.setTextSize(20);
            titleText.setText(viewPager.getAdapter().getPageTitle(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(leftMargin,5,rightMargin,5);

            linearLayout.addView(view,params);
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position+1<linearLayout.getChildCount()) {
            startX = (int) (linearLayout.getChildAt(position).getX() + (linearLayout.getChildAt(position).getWidth()+leftMargin+rightMargin) * positionOffset);
        }
        //往右滑动
        if(position ==currentPosition&&position+1<linearLayout.getChildCount() ){
            textWidth = (int) (linearLayout.getChildAt(position).getWidth()+((linearLayout.getChildAt(position+1).getWidth()
                    -linearLayout.getChildAt(position).getWidth())*positionOffset));
        }else if(position+1<linearLayout.getChildCount()){
            //向右滑动
            textWidth = (int) (linearLayout.getChildAt(position+1).getWidth()+((linearLayout.getChildAt(position).getWidth()-linearLayout.getChildAt(position+1).getWidth())*(1-positionOffset))
                    );
        }
        postInvalidate();
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        //获取所有标题 并遍历
        for (int i = 0; i <linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            TextView title = view.findViewById(R.id.indicator_item_text);
            if(i == position){
                //让当前指示器跟着选中的控件移动
                this.scrollTo((int) linearLayout.getChildAt(i).getX()+linearLayout.getChildAt(i).getMeasuredWidth()/2
                        - getMeasuredWidth()/2, 0);
                 startX = (int) linearLayout.getChildAt(i).getX();
                 textWidth = linearLayout.getChildAt(i).getMeasuredWidth();
                postInvalidate();
                title.setTextColor(Color.RED);
            }else {
                title.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制指示线
        canvas.drawLine(startX,getMeasuredHeight()-5,startX+textWidth,getMeasuredHeight()-5,paint);
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
