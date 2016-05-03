package com.example.vivi.letsgo.ui.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vivi.letsgo.model.GroupInfo;
import com.example.vivi.letsgo.ui.base.ListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ViVi on 2016/1/13.
 */
public class ListUserGroupFragment extends ListFragment {
    private SharedPreferences pref;
    private String userid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userid = pref.getString("userid", "");
    }

    @Override
    public StringRequest getHotGroupRequest(final String page,final boolean needRefresh) {
        StringRequest userGroupRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/getusergroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("")){
                            Toast.makeText(getActivity(),"Json Error NULL!Go Debug",Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject json= new JSONObject(response);
                                JSONArray jsonArray=json.getJSONArray("rows");
                                Gson gson = new Gson();
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
                                Toast.makeText(getActivity(),"Json Error EXAM!Go Debug",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT).show();
                if (myRefreshListView.isLoading)
                    myRefreshListView.setLoading(false);
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
}
