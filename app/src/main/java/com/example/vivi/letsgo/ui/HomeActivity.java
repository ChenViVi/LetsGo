package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.DrawerViewPagerAdapter;
import com.example.vivi.letsgo.ui.fragment.ListHotGroupFragment;
import com.example.vivi.letsgo.ui.fragment.ListUserGroupFragment;
import com.example.vivi.letsgo.ui.fragment.GroupClassifyFragment;
import com.example.vivi.letsgo.ui.fragment.DrawerFragment;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerFragment drawerFragment;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        findView();
        setView();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
    }

    private void setView() {
        //bar
        //getSupportActionBar().setTitle(R.string.nav_item_home);
        toolbar.setTitle("圈子");
        //viewpager
        setupViewPager();
        //Drawer
        setupDrawer();
    }

    private void setupDrawer() {
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(new DrawerFragment.FragmentDrawerListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                Intent intent;
                switch (position) {
                    case 1:
                        intent = new Intent(HomeActivity.this,ArticleActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        editor.putString("userid","");
                        editor.commit();
                        intent = new Intent(HomeActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    default:
                        break;
                }
            }
        });
    }
    private void setupViewPager() {
        DrawerViewPagerAdapter adapter = new DrawerViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListUserGroupFragment(), "我的圈子");
        adapter.addFragment(new ListHotGroupFragment(), "热门圈子");
        adapter.addFragment(new GroupClassifyFragment(), "圈子分区");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
