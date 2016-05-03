package com.example.vivi.letsgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.model.GroupMessageDiscuss;
import com.example.vivi.letsgo.model.UserInfoItem;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/4/12 0012.
 */
public class UserInfoAdapter extends BaseAdapter {

    ArrayList<UserInfoItem> data;
    Context context;

    class ViewHolder {
        TextView userInfoNameTv;
        TextView userInfoContentTv;
    }

    public UserInfoAdapter(Context context, ArrayList<UserInfoItem> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserInfoItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfoItem userInfoItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view= LayoutInflater.from(context).inflate(R.layout.item_user_info,null);
            viewHolder = new ViewHolder();
            viewHolder.userInfoNameTv = (TextView) view.findViewById(R.id.tv_user_info_name);
            viewHolder.userInfoContentTv = (TextView) view.findViewById(R.id.tv_user_info_content);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.userInfoNameTv.setText(userInfoItem.getName());
        viewHolder.userInfoContentTv.setText(userInfoItem.getContent());
        return view;
    }
}
