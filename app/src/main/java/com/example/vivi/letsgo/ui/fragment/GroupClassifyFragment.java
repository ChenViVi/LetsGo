package com.example.vivi.letsgo.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.ui.ListGroupInfoActivity;

/**
 * Created by ViVi on 2016/1/13.
 */
public class GroupClassifyFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout clubLayout;
    private RelativeLayout foodLayout;
    private RelativeLayout sportLayout;
    private RelativeLayout movieLayout;
    private RelativeLayout musicLayout;
    private RelativeLayout businessLayout;
    private RelativeLayout mixLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_group_classify, container, false);

        clubLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_club);
        foodLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_food);
        sportLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_sport);
        movieLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_movie);
        musicLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_music);
        businessLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_business);
        mixLayout = (RelativeLayout) layout.findViewById(R.id.layout_group_classify_mix);

        clubLayout.setOnClickListener(this);
        foodLayout.setOnClickListener(this);
        sportLayout.setOnClickListener(this);
        movieLayout.setOnClickListener(this);
        musicLayout.setOnClickListener(this);
        businessLayout.setOnClickListener(this);
        mixLayout.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ListGroupInfoActivity.class);
        String classifyId = null;
        String classifyName = null;
        switch (v.getId()){
            case R.id.layout_group_classify_club:
                classifyId = "2";
                classifyName = "社团圈";
                break;
            case R.id.layout_group_classify_food:
                classifyId = "3";
                classifyName = "美食圈";
                break;
            case R.id.layout_group_classify_sport:
                classifyId = "4";
                classifyName = "运动圈";
                break;
            case R.id.layout_group_classify_movie:
                classifyId = "5";
                classifyName = "电影圈";
                break;
            case R.id.layout_group_classify_music:
                classifyId = "6";
                classifyName = "音乐圈";
                break;
            case R.id.layout_group_classify_business:
                classifyId = "7";
                classifyName = "业务交流圈";
                break;
            case R.id.layout_group_classify_mix:
                classifyId = "8";
                classifyName = "综合圈";
                break;
        }
        intent.putExtra("classifyId",classifyId);
        intent.putExtra("classifyName",classifyName);
        startActivity(intent);
    }
}