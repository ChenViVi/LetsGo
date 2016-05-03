package com.example.vivi.letsgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.model.GroupMessageDiscuss;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/4/7 0007.
 */
public class GroupMessageDiscussAdapter extends BaseAdapter {

    ArrayList<GroupMessageDiscuss> data;
    Context context;

    class ViewHolder {
        TextView name;
        TextView content;
        TextView replyNameTv;
        TextView replyTextTv;
    }

    public GroupMessageDiscussAdapter(Context context, ArrayList<GroupMessageDiscuss> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GroupMessageDiscuss getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupMessageDiscuss discuss = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view= LayoutInflater.from(context).inflate(R.layout.item_group_message_discuss,null);
            viewHolder = new ViewHolder();
            viewHolder.content = (TextView) view.findViewById(R.id.tv_discuss_content);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_discuss_name);
            viewHolder.replyNameTv = (TextView) view.findViewById(R.id.tv_discuss_reply_name);
            viewHolder.replyTextTv = (TextView) view.findViewById(R.id.tv_discuss_reply_text);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.content.setText(": "+discuss.getContent());
        viewHolder.name.setText(discuss.getName());
        viewHolder.replyNameTv.setText(discuss.getTousermc());
        if(!viewHolder.replyNameTv.getText().equals("")){
            viewHolder.replyTextTv.setText("回复");
        }
        return view;
    }
}


