package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignActivity extends BackToolBarActivity implements TextWatcher {

    private EditText nickNameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText passwordRepeatEdit;
    private EditText nameEdit;
    private EditText studentNumEdit;
    private EditText idNumEdit;
    private Button signBtn;

    private boolean isEmpty;

    private RequestQueue mQueue;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        mQueue = Volley.newRequestQueue(this);

        nickNameEdit = (EditText) findViewById(R.id.edit_user_nick_name);
        emailEdit = (EditText) findViewById(R.id.edit_user_email);
        phoneEdit = (EditText) findViewById(R.id.edit_user_phone);
        passwordEdit = (EditText) findViewById(R.id.edit_user_password);
        passwordRepeatEdit = (EditText) findViewById(R.id.edit_user_password_repeat);
        nameEdit = (EditText) findViewById(R.id.edit_user_name);
        studentNumEdit = (EditText) findViewById(R.id.edit_user_student_number);
        idNumEdit = (EditText) findViewById(R.id.edit_user_id_number);
        signBtn = (Button) findViewById(R.id.btn_sign);

        nickNameEdit.addTextChangedListener(this);
        emailEdit.addTextChangedListener(this);
        phoneEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        passwordRepeatEdit.addTextChangedListener(this);
        nameEdit.addTextChangedListener(this);
        studentNumEdit.addTextChangedListener(this);
        idNumEdit.addTextChangedListener(this);

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = nickNameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String passwordRepeat = passwordRepeatEdit.getText().toString();
                String name = nameEdit.getText().toString();
                String studentNum = studentNumEdit.getText().toString();
                String idNum = idNumEdit.getText().toString();
                if (isEmpty){
                    Toast.makeText(SignActivity.this,"请填写所有信息",Toast.LENGTH_SHORT).show();
                }
                else if (!isEmail(email))
                    Toast.makeText(SignActivity.this,"请输入正确的邮箱号",Toast.LENGTH_SHORT).show();
                else if (!isMobileNum(phone))
                    Toast.makeText(SignActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                else if(password.length() < 6)
                    Toast.makeText(SignActivity.this,"请输入6位或6以上的密码",Toast.LENGTH_SHORT).show();
                else if (!password.equals(passwordRepeat))
                    Toast.makeText(SignActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                else if (studentNum.length() != 10)
                    Toast.makeText(SignActivity.this,"请输入正确的学号",Toast.LENGTH_SHORT).show();
                else if (idNum.length() != 15 && idNum.length() != 18)
                    Toast.makeText(SignActivity.this,"请输入正确的身份证号",Toast.LENGTH_SHORT).show();
                else
                    mQueue.add(getSigneRequest(nickName,email,phone,password,name,studentNum,idNum));
            }
        });
    }

    private StringRequest getSigneRequest(final String nickName,final String email,final String phone,final String password,final String name,final String studentNum,final String idNum){
        StringRequest signRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/ios/client/userreg",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        Toast.makeText(SignActivity.this,response,Toast.LENGTH_LONG).show();

                        String[] ary = response.split(";");
                        String signConfirm = ary[0];
                        switch (Integer.parseInt(signConfirm)){
                            case 0:Toast.makeText(SignActivity.this,"非法数据",Toast.LENGTH_SHORT).show();break;
                            case -1:Toast.makeText(SignActivity.this,"此电子邮箱已经注册",Toast.LENGTH_SHORT).show();break;
                            case -2:Toast.makeText(SignActivity.this,"此昵称已经占用",Toast.LENGTH_SHORT).show();break;
                            case -4:Toast.makeText(SignActivity.this,"此身份证已经注册",Toast.LENGTH_SHORT).show();break;
                            case -5:Toast.makeText(SignActivity.this,"此电话号码已经注册",Toast.LENGTH_SHORT).show();break;
                            case -6:Toast.makeText(SignActivity.this,"此姓名、学号和身份证号无法通过学籍库的认证",Toast.LENGTH_SHORT).show();break;
                            case 1:String userInfoJson = ary[1];
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
                                    Intent intent = new Intent(SignActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    WelcomeActivity.welcomeActivityInstance.finish();
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignActivity.this,"网络连接超时",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("content","email="+email+"&nickname="+nickName+"&name="+name+"&studno="+studentNum+"&idcardno="+idNum+"&phone="+phone+"&passwd="+password);
                return map;
            }
        };
        return signRequest;
    }

    private static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String nickName = nickNameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String passwordRepeat = passwordRepeatEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String studentNum = studentNumEdit.getText().toString();
        String idNum = idNumEdit.getText().toString();
        if (nickName.isEmpty()||email.isEmpty()||phone.isEmpty()||password.isEmpty()||passwordRepeat.isEmpty()||name.isEmpty()||studentNum.isEmpty()||idNum.isEmpty()){
            signBtn.setBackgroundResource(R.drawable.bg_btn_dark_disable);
            isEmpty = true;
        }

        else {
            signBtn.setBackgroundResource(R.drawable.bg_btn_dark_enable);
            isEmpty = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign, menu);
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
