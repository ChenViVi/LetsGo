package com.example.vivi.letsgo.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.vivi.letsgo.util.BackToolBarUtil;

/**
 * Created by ViVi on 2016/1/16.
 */
public abstract class BackToolBarActivity extends AppCompatActivity {
    private BackToolBarUtil mBackToolBarHelper;
    public Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {

        mBackToolBarHelper = new BackToolBarUtil(this,layoutResID) ;
        toolbar = mBackToolBarHelper.getToolBar() ;
        setContentView(mBackToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;
    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}
