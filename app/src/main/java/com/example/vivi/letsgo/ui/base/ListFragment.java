package com.example.vivi.letsgo.ui.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.GroupInfoAdapter;
import com.example.vivi.letsgo.model.GroupInfo;
import com.example.vivi.letsgo.ui.ListGroupMessageActivity;
import com.example.vivi.letsgo.ui.view.RefreshLayout;
import com.example.vivi.letsgo.util.BitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ViVi on 2016/1/13.
 */
public class ListFragment extends Fragment {

    public int page = 1;
    public List<GroupInfo> data=new ArrayList<>();

    private RequestQueue mQueue;
    private ImageLoader imageLoader;

    private ListView listView;
    public BaseAdapter adapter;
    public RefreshLayout myRefreshListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = Volley.newRequestQueue(getActivity());
        imageLoader = new ImageLoader(mQueue,new BitmapCache());
        mQueue.add(getHotGroupRequest(String.valueOf(page), false));
        adapter=new GroupInfoAdapter(getActivity(),R.layout.item_group,data,imageLoader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_list, container, false);

        listView=(ListView)layout.findViewById(R.id.list_view);
        myRefreshListView = (RefreshLayout) layout.findViewById(R.id.swipe_layout);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GroupInfo groupInfo = data.get(position);
                Intent intent = new Intent(getActivity(), ListGroupMessageActivity.class);
                intent.putExtra("groupid", groupInfo.getGroupid());
                intent.putExtra("groupname", groupInfo.getName());
                startActivity(intent);
            }
        });

        myRefreshListView.setColorSchemeColors(R.color.colorPrimary);
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueue.add(mQueue.add(getHotGroupRequest(String.valueOf("1"),true)));
            }
        });

        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mQueue.add(getHotGroupRequest(String.valueOf(++page),false));
            }
        });
        return layout;
    }

    public StringRequest getHotGroupRequest(final String page, final boolean needRefresh){
        return null;
    }
}