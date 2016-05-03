package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.model.UserInfo;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends BackToolBarActivity{

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button loginBtn;

    private RequestQueue mQueue;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        mQueue = Volley.newRequestQueue(this);
        accountEdit = (EditText) findViewById(R.id.edit_user_account);
        passwordEdit = (EditText) findViewById(R.id.edit_user_password);
        loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                getLoginRequest(account, password);
                mQueue.add(getLoginRequest(account, password));
            }
        });
    }

    private StringRequest getLoginRequest(final String account,final String password){
        StringRequest loginRequest = new StringRequest(Request.Method.GET,"http://letsgo966.vipsinaapp.com/index.php/ios/client/login?logincode="+account+"&pwd="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        if (response.equals("")){
                            Toast.makeText(LoginActivity.this, "Json Error NULL!Go Debug", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String[] ary = response.split(";");
                            String loginConfirm = ary[0];
                            if (loginConfirm.equals("0")){
                                Toast.makeText(LoginActivity.this,"无效的用户名或密码",Toast.LENGTH_LONG).show();
                            }
                            else {
                                String userInfoJson = ary[1];
                                JSONObject json= null;
                                try {
                                    json = new JSONObject(userInfoJson);
                                    JSONArray jsonArray=json.getJSONArray("rows");
                                    Gson gson = new Gson();
                                    List<UserInfo> items = gson.fromJson(jsonArray.toString(), new TypeToken<List<UserInfo>>() {}.getType());
                                    UserInfo userInfo = items.get(0);
                                    editor.putString("userid", userInfo.getId());
                                    editor.putString("headimg01", userInfo.getHeadimg00());
                                    editor.putString("nickname", userInfo.getNickname());
                                    editor.putString("signature",userInfo.getSignature());
                                    editor.putString("address",userInfo.getHometown());
                                    editor.putString("sex",userInfo.getSex());
                                    editor.putString("hobby",userInfo.getHobby());
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    WelcomeActivity.welcomeActivityInstance.finish();
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"网络连接超时",Toast.LENGTH_SHORT).show();
            }
        });
        return loginRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
