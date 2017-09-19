package com.example.viewpagerindicatordemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.viewpagerindicatordemo.view.SimpleViewPagerIndicator;
import com.example.viewpagerindicatordemo.view.ViewPagerIndicator;

public class MainActivity extends AppCompatActivity {


    ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private String [] frag = {"头条","八卦","娱乐","亲子节目","大漩涡","我的收藏","系统文件","设置"};
    private ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return NewFragment.getInstance(frag[position]);
            }

            @Override
            public int getCount() {
                return frag.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return frag[position];
            }
        };
        viewPager.setAdapter(adapter);
        viewPagerIndicator.setViewPager(viewPager);

    }
}
