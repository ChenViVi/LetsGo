package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.GroupMessageAdapter;
import com.example.vivi.letsgo.model.GroupMessage;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;
import com.example.vivi.letsgo.ui.view.MyRefreshLayout;
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

public class ListGroupMessageActivity extends BackToolBarActivity {

    public static ListGroupMessageActivity listGroupMessageInstance = null;

    private String groupid;
    private String groupname;

    private int page = 1  ;
    private List<GroupMessage> data=new ArrayList<GroupMessage>();
    public String postid = "";
    public String touser = "";
    private String userid;

    private RequestQueue mQueue;
    private ImageLoader imageLoader;

    private SharedPreferences pref;

    private ListView listView;
    private GroupMessageAdapter adapter;
    private MyRefreshLayout myRefreshListView;
    private LinearLayout groupMessageDiscussLayout;
    private EditText discussEdit;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_group_message);

        listGroupMessageInstance = this;
        mQueue = Volley.newRequestQueue(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        groupid = intent.getStringExtra("groupid");
        groupname = intent.getStringExtra("groupname");
        getSupportActionBar().setTitle(groupname);
        userid = pref.getString("userid", "");


        imageLoader = new ImageLoader(mQueue,new BitmapCache());
        mQueue.add(getGroupMessageRequest(String.valueOf(page),false));
        adapter=new GroupMessageAdapter(this,data,imageLoader,mQueue,userid);

        listView=(ListView) findViewById(R.id.list_view);
        myRefreshListView = (MyRefreshLayout) findViewById(R.id.swipe_layout);
        groupMessageDiscussLayout = (LinearLayout) findViewById(R.id.layout_group_message_discuss);
        discussEdit = (EditText) findViewById(R.id.edit_discuss);
        sendBtn = (Button) findViewById(R.id.btn_send);

        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueue.add(mQueue.add(getGroupMessageRequest(String.valueOf("1"), true)));
            }
        });

        myRefreshListView.setOnLoadListener(new MyRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mQueue.add(getGroupMessageRequest(String.valueOf(++page), false));
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = discussEdit.getText().toString();
                if (!content.equals("")) {
                    mQueue.add(getAddDiscussRequest(groupid, postid, userid, content, touser));
                    Log.e("groupid", groupid);
                    Log.e("postid",postid);
                    Log.e("userid",userid);
                    Log.e("content",content);
                    Log.e("touser",touser.toString());
                    onFocusChange(false);
                } else Toast.makeText(ListGroupMessageActivity.this, "评论不能为空", Toast.LENGTH_LONG).show();
            }
        });

        listView.setAdapter(adapter);
    }


    public void onFocusChange(boolean hasFocus){
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        discussEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    groupMessageDiscussLayout.setVisibility(View.VISIBLE);
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    discussEdit.setFocusable(true);
                    discussEdit.setFocusableInTouchMode(true);
                    discussEdit.requestFocus();
                } else {
                    discussEdit.setText("");
                    groupMessageDiscussLayout.setVisibility(View.GONE);
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(discussEdit.getWindowToken(), 0);
                }
            }
        }, 100);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("needRefresh", false)){
            data.clear();
            mQueue.add(getGroupMessageRequest("1",false));
        }
    }

    private StringRequest getAddDiscussRequest(final String groupid, final String postid, final String userid, final String content, final String touser){
        StringRequest addDiscussRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/android/general/adddiscuss",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListGroupMessageActivity.this,"网络连接超时",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("groupid",groupid);
                map.put("postid",postid);
                map.put("userid",userid);
                map.put("content",content);
                map.put("touser",touser);
                map.put("signature","6CF5BBB6C7318A26B4D42FCC285ECA2A");
                return map;
            }
        };
        return addDiscussRequest;
    }

    private StringRequest getGroupMessageRequest(final String page, final boolean needRefresh){
        StringRequest groupMessageRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/getgrouppost44",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        if (response.equals("")){
                            Toast.makeText(ListGroupMessageActivity.this,"Json Error NULL!Go Debug",Toast.LENGTH_LONG).show();
                        }
                        else {
                            String[] ary = response.split(";");
                            String s  = ary[1];

                            try{
                                JSONObject json= new JSONObject(s);
                                JSONArray jsonArray=json.getJSONArray("rows");
                                Gson gson = new Gson();
                                List<GroupMessage> items = gson.fromJson(jsonArray.toString(), new TypeToken<List<GroupMessage>>() {
                                }.getType());
                                if (needRefresh){
                                    data.clear();
                                }
                                for (GroupMessage groupMessage:items){
                                    data.add(groupMessage);
                                }
                                adapter.notifyDataSetChanged();
                                if (myRefreshListView.isLoading)
                                    myRefreshListView.setLoading(false);
                                myRefreshListView.setRefreshing(false);
                            }
                            catch (JSONException e){
                                Toast.makeText(ListGroupMessageActivity.this,"Json Error EXAM!Go Debug",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListGroupMessageActivity.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                if (myRefreshListView.isLoading)
                    myRefreshListView.setLoading(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("loginid","ABCDEF-EFGHE");
                map.put("groupid",groupid);
                map.put("groupname",groupname);
                map.put("page",page);
                return map;
            }
        };
        return groupMessageRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_add){
            Intent intent = new Intent(this,AddGroupMessageActivity.class);
            intent.putExtra("groupid",groupid);
            intent.putExtra("groupname",groupname);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
