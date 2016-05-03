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
import com.example.vivi.letsgo.adapter.ArticleInfoAdapter;
import com.example.vivi.letsgo.model.ArticleInfo;
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

public class ArticleActivity extends BackToolBarActivity {

    private int page = 1;

    private List<ArticleInfo> data=new ArrayList<ArticleInfo>();

    private RequestQueue mQueue;
    private ImageLoader imageLoader;
    private Gson gson = new Gson();

    private ListView listView;
    private ArticleInfoAdapter adapter;
    private RefreshLayout myRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        mQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(mQueue,new BitmapCache());
        mQueue.add(getArticleInfoRequest(String.valueOf(page),false));
        adapter=new ArticleInfoAdapter(this,R.layout.item_article_info,data,imageLoader);

        listView=(ListView) findViewById(R.id.list_view);
        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArticleInfo articleInfo = data.get(position);
                Intent intent = new Intent(ArticleActivity.this, WebActivity.class);
                intent.putExtra("articleId", articleInfo.getArtid());
                intent.putExtra("title", articleInfo.getTitle());
                startActivity(intent);
            }
        });

        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                mQueue.add(mQueue.add(getArticleInfoRequest(String.valueOf(page),true)));
            }
        });

        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mQueue.add(getArticleInfoRequest(String.valueOf(++page),false));
            }
        });
    }

    private StringRequest getArticleInfoRequest(final String page,final boolean needRefresh){
        StringRequest articleInfoRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/artrecommend39",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("")){
                            Toast.makeText(ArticleActivity.this, "Json Error NULL!Go Debug", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject json= new JSONObject(response);
                                JSONArray jsonArray=json.getJSONArray("rows");
                                List<ArticleInfo> items = gson.fromJson(jsonArray.toString(), new TypeToken<List<ArticleInfo>>() {}.getType());
                                if (needRefresh){
                                    data.clear();
                                }
                                for (int i = 1;i<items.size();i++){
                                    data.add(items.get(i));
                                }
                                adapter.notifyDataSetChanged();
                                if (myRefreshListView.isLoading)
                                    myRefreshListView.setLoading(false);
                                myRefreshListView.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ArticleActivity.this,"Json Error EXAM!Go Debug",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArticleActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("signature","6CF5BBB6C7318A26B4D42FCC285ECA2A");
                map.put("page",page);
                return map;
            }
        };
        return articleInfoRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
