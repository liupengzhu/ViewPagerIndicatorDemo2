package com.example.viewpagerindicatordemo.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    private int currentPosition;
    private int leftMargin;
    private int rightMargin;
    private int titleColor;
    private int titleSelectionColor;
    private int titleTextSize;
    private int indicatorColor;
    private int topMargin;
    private int bottomMargin;
    private int indicatorSize;

    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }



    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        if(array!=null){
            leftMargin = (int) array.getDimension(R.styleable.ViewPagerIndicator_marginLeft,10);
            rightMargin = (int) array.getDimension(R.styleable.ViewPagerIndicator_marginRight,10);
            titleColor = array.getColor(R.styleable.ViewPagerIndicator_titleColor,Color.BLACK);
            titleSelectionColor = array.getColor(R.styleable.ViewPagerIndicator_titleSelectionColor,Color.RED);
            titleTextSize = (int) array.getDimension(R.styleable.ViewPagerIndicator_titleTextSize,20);
            indicatorColor =  array.getColor(R.styleable.ViewPagerIndicator_indicatorColor,Color.RED);
            topMargin = (int) array.getDimension(R.styleable.ViewPagerIndicator_marginTop,5);
            bottomMargin = (int) array.getDimension(R.styleable.ViewPagerIndicator_marginBottom,5);
            indicatorSize = (int) array.getDimension(R.styleable.ViewPagerIndicator_indicatorSize,3);
            array.recycle();
        }

        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(indicatorSize);
        paint.setColor(indicatorColor);
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
            titleText.setTextColor(titleColor);
            titleText.setTextSize(titleTextSize);
            if(i==0){
                titleText.setTextColor(titleSelectionColor);
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

            titleText.setText(viewPager.getAdapter().getPageTitle(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);

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
                title.setTextColor(titleSelectionColor);
            }else {
                title.setTextColor(titleColor);
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
