package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.ImageViewPagerAdapter;
import com.example.vivi.letsgo.util.BitmapCache;

import java.util.ArrayList;
import java.util.List;

public class ListGroupMessageImageActivity extends AppCompatActivity {

    private ViewPager imageViewPager;
    ImageView[] pointList;

    private RequestQueue mQueue;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_group_message_image);

        mQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(mQueue,new BitmapCache());

        Intent intent = getIntent();
        ArrayList<String> imgUrlList = intent.getStringArrayListExtra("imgUrlList");
        int imgCount = intent.getIntExtra("imgCount",0);
        int imageId = intent.getIntExtra("imageId", 0);

        List<View> groupMessageImageList = new ArrayList<>();
        for(int i = 0; i < imgCount; i++) {
            NetworkImageView groupMessageImage = new NetworkImageView(ListGroupMessageImageActivity.this);
            groupMessageImage.setImageUrl(imgUrlList.get(i), imageLoader);
            groupMessageImageList.add(groupMessageImage);
        }

        initViewPager(groupMessageImageList,imageId);
    }

    private void initViewPager(List<View> groupMessageImageList,int imageId) {

        imageViewPager = (ViewPager) findViewById(R.id.group_message_image_pager);
        ViewGroup group = (ViewGroup) findViewById(R.id.layout_point);

        //对imageviews进行填充
        pointList = new ImageView[groupMessageImageList.size()];

        //小图标
        for (int i = 0; i < groupMessageImageList.size(); i++) {
            ImageView groupMessageImage = new ImageView(this);
            groupMessageImage.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            groupMessageImage.setPadding(5, 5, 5, 5);
            pointList[i] = groupMessageImage;
            if (i == 0) {
                pointList[i].setBackgroundResource(R.drawable.bg_point_focus);
            } else {
                pointList[i].setBackgroundResource(R.drawable.bg_point_defualt);
            }
            group.addView(pointList[i]);
        }

        imageViewPager.setAdapter(new ImageViewPagerAdapter(groupMessageImageList));
        for (int i = 0; i < pointList.length; i++) {
            imageViewPager.setCurrentItem(imageId);
            pointList[imageId].setBackgroundResource(R.drawable.bg_point_focus);
            if (imageId != i) {
                pointList[i].setBackgroundResource(R.drawable.bg_point_defualt);
            }
        }
        imageViewPager.setOnPageChangeListener(new GuidePageChangeListener());
        imageViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < pointList.length; i++) {
                imageViewPager.setCurrentItem(arg0);
                pointList[arg0].setBackgroundResource(R.drawable.bg_point_focus);
                if (arg0 != i) {
                    pointList[i].setBackgroundResource(R.drawable.bg_point_defualt);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_group_message_image, menu);
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
