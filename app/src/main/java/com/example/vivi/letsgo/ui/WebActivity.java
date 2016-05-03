package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;

public class WebActivity extends BackToolBarActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        String articleId = intent.getStringExtra("articleId");
        String articleTitle = intent.getStringExtra("title");
        webView.loadUrl("http://letsgo966.vipsinaapp.com/index.php/android/staticart?id="+articleId+"&source=app&loginid=1");
        getSupportActionBar().setTitle(articleTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
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
