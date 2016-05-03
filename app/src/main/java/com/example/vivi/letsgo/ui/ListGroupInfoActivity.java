package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.vivi.letsgo.adapter.GroupInfoAdapter;
import com.example.vivi.letsgo.model.GroupInfo;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;
import com.example.vivi.letsgo.ui.view.RefreshLayout;
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

public class ListGroupInfoActivity extends BackToolBarActivity {

    private String classifyId;
    private String classifyName;

    private int page = 1;

    private List<GroupInfo> data=new ArrayList<GroupInfo>();

    private RequestQueue mQueue;
    private ImageLoader imageLoader;
    private Gson gson = new Gson();

    private ListView listView;
    private GroupInfoAdapter adapter;
    private RefreshLayout myRefreshListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        Intent intent = getIntent();
        classifyId = intent.getStringExtra("classifyId");
        classifyName = intent.getStringExtra("classifyName");

        getSupportActionBar().setTitle(classifyName);

        mQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(mQueue,new BitmapCache());
        mQueue.add(getGroupInfoRequest(String.valueOf(page),false));
        adapter=new GroupInfoAdapter(this,R.layout.item_group,data,imageLoader);

        listView=(ListView) findViewById(R.id.list_view);
        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GroupInfo groupInfo = data.get(position);
                Intent intent = new Intent(ListGroupInfoActivity.this, ListGroupMessageActivity.class);
                intent.putExtra("groupid", groupInfo.getGroupid());
                intent.putExtra("groupname", groupInfo.getName());
                startActivity(intent);
            }
        });

        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueue.add(mQueue.add(getGroupInfoRequest(String.valueOf("1"),true)));
            }
        });

        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (classifyId.equals("5")){mQueue.add(getGroupInfoRequest(String.valueOf(++page),false));}
                else mQueue.add(getGroupInfoRequest(String.valueOf(++page),true));
            }
        });
    }

    private StringRequest getGroupInfoRequest(final String page, final boolean needRefresh){
        StringRequest postRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/getgroupbyclassify",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("")){
                            Toast.makeText(ListGroupInfoActivity.this,"Json Error NULL!Go Debug",Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject json= new JSONObject(response);
                                JSONArray jsonArray=json.getJSONArray("rows");
                                List<GroupInfo> items = gson.fromJson(jsonArray.toString(), new TypeToken<List<GroupInfo>>() {}.getType());
                                if (needRefresh){
                                    data.clear();
                                }
                                for (GroupInfo groupInfo:items){
                                    data.add(groupInfo);
                                }
                                adapter.notifyDataSetChanged();
                                if (myRefreshListView.isLoading)
                                    myRefreshListView.setLoading(false);
                                myRefreshListView.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ListGroupInfoActivity.this,"Json Error EXAM!Go Debug",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListGroupInfoActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                myRefreshListView.setLoading(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("school","10152");
                map.put("classify",classifyId);
                map.put("page",page);
                return map;
            }
        };
        return postRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_info, menu);
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
