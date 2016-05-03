package com.example.vivi.letsgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.model.GroupMessage;
import com.example.vivi.letsgo.model.GroupMessageDiscuss;
import com.example.vivi.letsgo.ui.ListGroupMessageActivity;
import com.example.vivi.letsgo.ui.ListGroupMessageImageActivity;
import com.example.vivi.letsgo.ui.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vivi on 2016/5/1.
 */
public class GroupMessageAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;
    private List<GroupMessage> data;
    private RequestQueue mQueue;
    private String userid;

    public GroupMessageAdapter(Context context, List<GroupMessage> data, ImageLoader imageLoader, RequestQueue mQueue, String userid){
        layoutInflater = LayoutInflater.from(context);
        this.imageLoader = imageLoader;
        this.data = data;
        this.context = context;
        this.mQueue = mQueue;
        this.userid = userid;
    }

    class ViewHolder{
        TextView messageUserName;
        TextView messageDate;
        TextView messageContent;
        NetworkImageView headImage;
        NoScrollListView listView;
        Button commentBtn;
        NetworkImageView[] images = new NetworkImageView[9];
        ViewHolder(View view){
            messageUserName = (TextView) view.findViewById(R.id.tv_message_user_name);
            messageContent = (TextView) view.findViewById(R.id.tv_message_content);
            messageDate = (TextView) view.findViewById(R.id.tv_message_date);
            headImage = (NetworkImageView) view.findViewById(R.id.nimg_headimg);
            listView = (NoScrollListView) view.findViewById(R.id.layout_group_message_discuss);
            commentBtn = (Button) view.findViewById(R.id.btn_comment);
            images[0] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_00);
            images[1] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_01);
            images[2] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_02);
            images[3] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_03);
            images[4] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_04);
            images[5] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_05);
            images[6] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_06);
            images[7] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_07);
            images[8] = (NetworkImageView) view.findViewById(R.id.nimg_message_image_08);
        }
    }

    private class OnImageClickListener implements View.OnClickListener {
        private int imgCount;
        private int imageId;
        private ArrayList<String> imgUrlList;
        public OnImageClickListener(int imgCount, int imageId, ArrayList<String> imgUrlList){

            this.imgCount = imgCount;
            this.imageId = imageId;
            this.imgUrlList = imgUrlList;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,ListGroupMessageImageActivity.class);
            intent.putExtra("imageId", imageId);
            intent.putExtra("imgCount", imgCount);
            intent.putStringArrayListExtra("imgUrlList",imgUrlList);
            context.startActivity(intent);
        }
    }

    private class OnCommentClickListener implements View.OnClickListener {
        private GroupMessage groupMessage;
        public OnCommentClickListener(GroupMessage groupMessage){
            this.groupMessage = groupMessage;
        }
        @Override
        public void onClick(View v) {
            ListGroupMessageActivity.listGroupMessageInstance.onFocusChange(true);
            ListGroupMessageActivity.listGroupMessageInstance.postid = groupMessage.getPostid();
            ListGroupMessageActivity.listGroupMessageInstance.touser = "";
        }
    }

    private StringRequest getGroupMessageDiscussRequest(final String postid, final NoScrollListView listView){
        StringRequest groupMessageDiscussRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/getpostdiscuss",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        try {
                            JSONObject json= new JSONObject(response);
                            JSONArray jsonArray=json.getJSONArray("rows");

                            final ArrayList<GroupMessageDiscuss> discussList = gson.fromJson(jsonArray.toString(), new TypeToken<List<GroupMessageDiscuss>>() {}.getType());
                            listView.setAdapter(new GroupMessageDiscussAdapter(context,discussList));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    if (!userid.equals(discussList.get(position).getUserid())){
                                        ListGroupMessageActivity.listGroupMessageInstance.onFocusChange(true);
                                        ListGroupMessageActivity.listGroupMessageInstance.postid = postid;
                                        ListGroupMessageActivity.listGroupMessageInstance.touser = discussList.get(position).getUserid();
                                    }
                                    else ListGroupMessageActivity.listGroupMessageInstance.onFocusChange(false);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Json Error EXAM!Go Debug", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("postid",postid);
                return map;
            }
        };
        return groupMessageDiscussRequest;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /** 该方法返回多少个不同的布局*/
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 10;
    }

    /** 根据position返回相应的Item*/
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        GroupMessage groupMessage = data.get(position);
        if (groupMessage.getImg04() == null){
            if (groupMessage.getImg02() == null){
                if (groupMessage.getImg01() ==null){
                    if (groupMessage.getImg00() == null)
                        return 0;
                    else return 1;
                }
                else return 2;
            }
            else if(groupMessage.getImg03() ==null)
                return 3;
            else return 4;
        }
        else if (groupMessage.getImg06() ==null){
            if (groupMessage.getImg05() == null)
                return 5;
            else return 6;
        }
        else if (groupMessage.getImg08() == null){
            if (groupMessage.getImg07() == null)
                return 7;
            else return 8;
        }
        else return 9;
    }

    @Override
    public GroupMessage getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        GroupMessage groupMessage = getItem(position);

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_group_message,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.headImage.setDefaultImageResId(R.drawable.default_headimg);
        viewHolder.headImage.setErrorImageResId(R.drawable.default_headimg);
        viewHolder.headImage.setImageUrl("http://letsgo966-letsgo.stor.sinaapp.com/headimgs/" + groupMessage.getHeadimg() + ".png", imageLoader);
        viewHolder.messageUserName.setText(groupMessage.getName());
        viewHolder.messageDate.setText(groupMessage.getOpttime());
        viewHolder.messageContent.setText(groupMessage.getContent());

        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyyMM");

        ArrayList<String> imageUrl = new ArrayList<>();
        try {
            Date date = beforeFormat.parse(groupMessage.getOpttime());
            String imgDate = afterFormat.format(date);
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg00());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg01());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg02());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg03());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg04());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg05());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg06());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg07());
            imageUrl.add("http://letsgo966-letsgo.stor.sinaapp.com/postimgs/" + imgDate + "/" + groupMessage.getImg08());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int imgCount = getItemViewType(position);
        for (int i = 0; i < imgCount; i++){
            viewHolder.images[i].setVisibility(View.VISIBLE);
            viewHolder.images[i].setDefaultImageResId(R.drawable.default_headimg);
            viewHolder.images[i].setErrorImageResId(R.drawable.default_headimg);
            viewHolder.images[i].setImageUrl(imageUrl.get(i), imageLoader);
            viewHolder.images[i].setOnClickListener( new OnImageClickListener(imgCount,i,imageUrl));
        }
        viewHolder.commentBtn.setOnClickListener(new OnCommentClickListener(groupMessage));
        mQueue.add(getGroupMessageDiscussRequest(groupMessage.getPostid(), viewHolder.listView));
        return convertView;
    }
}
