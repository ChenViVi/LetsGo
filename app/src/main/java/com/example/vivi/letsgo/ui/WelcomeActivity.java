package com.example.vivi.letsgo.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vivi.letsgo.R;

public class WelcomeActivity extends Activity implements View.OnClickListener {

    public static WelcomeActivity welcomeActivityInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = pref.getString("userid", "");
        if (!userid.equals("")){
            Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        Button signBtn = (Button) findViewById(R.id.btn_sign);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        signBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        welcomeActivityInstance = this;
        if (v.getId() == R.id.btn_sign)
            startActivity(new Intent(this, SignActivity.class));
        else startActivity(new Intent(this, LoginActivity.class));
    }

}
