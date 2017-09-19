package com.example.viewpagerindicatordemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/19/019.
 */

public class NewFragment extends android.support.v4.app.Fragment {


    public NewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        String title = getArguments().getString("title");
        textView.setText(title);
        return textView;
    }
    public static NewFragment getInstance(String title){
        NewFragment fragment = new NewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }
}
