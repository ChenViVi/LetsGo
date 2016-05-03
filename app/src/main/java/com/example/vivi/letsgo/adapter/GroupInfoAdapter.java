package com.example.vivi.letsgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.model.GroupInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/6/11.
 */
public class GroupInfoAdapter extends ArrayAdapter<GroupInfo> {
    private int resourceId;
    private ImageLoader imageLoader;

    class ViewHolder {
        TextView groupName;
        TextView groupIntroduce;
        NetworkImageView groupImage;
    }

    public GroupInfoAdapter(Context context, int textViewResourceId, List<GroupInfo> objects, ImageLoader imageLoader) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        this.imageLoader = imageLoader;
    }

    public View getView(int position,View convertView,ViewGroup parent) {
        GroupInfo groupInfo = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.groupImage =(NetworkImageView)view.findViewById(R.id.nimg_hot_group_headimg);
            viewHolder.groupImage.setDefaultImageResId(R.drawable.default_headimg);
            viewHolder.groupImage.setErrorImageResId(R.drawable.default_headimg);
            viewHolder.groupName =(TextView)view.findViewById(R.id.tv_hot_group_name);
            viewHolder.groupIntroduce =(TextView)view.findViewById(R.id.tv_hot_group_introduce);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.groupImage.setImageUrl(groupInfo.getHeadimgurl(), imageLoader);
        viewHolder.groupName.setText(groupInfo.getName());
        viewHolder.groupIntroduce.setText(groupInfo.getIntroduce());
        return view;
    }
}