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
import com.example.vivi.letsgo.model.ArticleInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/6/11.
 */
public class ArticleInfoAdapter extends ArrayAdapter<ArticleInfo> {
    private int resourceId;
    private ImageLoader imageLoader;

    class ViewHolder {
        TextView articleTitle;
        TextView articleDate;
        NetworkImageView articleImage;
    }

    public ArticleInfoAdapter(Context context, int textViewResourceId, List<ArticleInfo> objects, ImageLoader imageLoader) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        this.imageLoader = imageLoader;
    }

    public View getView(int position,View convertView,ViewGroup parent) {
        ArticleInfo articleInfo=getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.articleImage=(NetworkImageView)view.findViewById(R.id.nimg_article_image);
            viewHolder.articleImage.setDefaultImageResId(R.drawable.default_article_image);
            viewHolder.articleImage.setErrorImageResId(R.drawable.default_article_image);
            viewHolder.articleTitle=(TextView)view.findViewById(R.id.tv_article_title);
            viewHolder.articleDate=(TextView)view.findViewById(R.id.tv_article_date);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.articleImage.setImageUrl(articleInfo.getImgurl(), imageLoader);
        viewHolder.articleTitle.setText(articleInfo.getTitle());
        viewHolder.articleDate.setText(articleInfo.getUpdatetime());
        return view;
    }
}