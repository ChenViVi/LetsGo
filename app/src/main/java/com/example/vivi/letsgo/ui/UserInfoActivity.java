package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
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
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.UserGroupAdapter;
import com.example.vivi.letsgo.adapter.UserInfoAdapter;
import com.example.vivi.letsgo.model.GroupInfo;
import com.example.vivi.letsgo.model.UserInfoItem;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;
import com.example.vivi.letsgo.ui.view.NoScrollListView;
import com.example.vivi.letsgo.util.BitmapCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoActivity extends BackToolBarActivity{

    private NetworkImageView userHeadImage;
    private TextView userNameTv;
    private TextView userSignatureTv;
    private NoScrollListView userBriefListView;
    private NoScrollListView userGroupListView;
    private ScrollView scrollView;

    private SharedPreferences pref;
    private RequestQueue mQueue;

    private List<GroupInfo> data=new ArrayList<GroupInfo>();
    private String userid;
    private UserGroupAdapter adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mQueue = Volley.newRequestQueue(this);

        userid = pref.getString("userid", "");

        userHeadImage = (NetworkImageView)findViewById(R.id.nimg_user_image);
        userNameTv = (TextView) findViewById(R.id.tv_user_name);
        userSignatureTv = (TextView) findViewById(R.id.tv_user_signature);
        userBriefListView = (NoScrollListView) findViewById(R.id.list_user_brief);
        userGroupListView = (NoScrollListView) findViewById(R.id.list_user_status);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        userHeadImage.setImageUrl("http://letsgo966-letsgo.stor.sinaapp.com/headimgs/" + pref.getString("headimg01", "") + ".png", new ImageLoader(mQueue, new BitmapCache()));
        userHeadImage.setDefaultImageResId(R.drawable.ic_profile);
        userHeadImage.setErrorImageResId(R.drawable.ic_profile);

        userNameTv.setText(pref.getString("nickname", ""));
        userSignatureTv.setText(pref.getString("signature", "这个人很懒，什么都没有写"));
        String sex = pref.getString("sex","");
        if (sex.equals("1"))
            sex = "男";
        else sex = "女";
        String address = pref.getString("address","");
        String hobby = pref.getString("hobby","");

        ArrayList<UserInfoItem> userBriefArrayList = new ArrayList<>();
        userBriefArrayList.add(new UserInfoItem("性别", sex));
        userBriefArrayList.add(new UserInfoItem("家乡", address));
        userBriefArrayList.add(new UserInfoItem("兴趣", hobby));
        userBriefListView.setAdapter(new UserInfoAdapter(this, userBriefArrayList));
        adapter = new UserGroupAdapter(UserInfoActivity.this,R.layout.item_user_group,data,new ImageLoader(mQueue,new BitmapCache()));
        userGroupListView.setAdapter(adapter);
        userGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupInfo groupInfo = data.get(position);
                Intent intent = new Intent(UserInfoActivity.this, ListGroupMessageActivity.class);
                intent.putExtra("groupid", groupInfo.getGroupid());
                intent.putExtra("groupname", groupInfo.getName());
                startActivity(intent);
            }
        });
        mQueue.add(getUserGroupRequest(String.valueOf(page)));
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int scrollY=v.getScrollY();
                        int height=v.getHeight();
                        int scrollViewMeasuredHeight=scrollView.getChildAt(0).getMeasuredHeight();
                        if((scrollY+height)==scrollViewMeasuredHeight){
                            mQueue.add(getUserGroupRequest(String.valueOf(++page)));
                        }
                        break;
                }
                return false;
            }
        });
    }

    private StringRequest getUserGroupRequest(final String page){
        StringRequest userGroupRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/getusergroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("")){
                            Toast.makeText(UserInfoActivity.this,"Json Error NULL!Go Debug",Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject json= new JSONObject(response);
                                JSONArray jsonArray=json.getJSONArray("rows");
                                Gson gson = new Gson();
                                List<GroupInfo> items = gson.fromJson(jsonArray.toString(), new TypeToken<List<GroupInfo>>() {}.getType());
                                for (GroupInfo groupInfo:items){
                                    data.add(groupInfo);
                                }
                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(UserInfoActivity.this,"Json Error EXAM!Go Debug",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserInfoActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("userid",userid);
                map.put("page",page);
                return map;
            }
        };
        return userGroupRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
