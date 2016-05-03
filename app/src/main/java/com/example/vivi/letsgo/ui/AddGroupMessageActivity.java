package com.example.vivi.letsgo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vivi.letsgo.R;
import com.example.vivi.letsgo.adapter.PhotoInfoAdapter;
import com.example.vivi.letsgo.model.MessageImage;
import com.example.vivi.letsgo.ui.base.BackToolBarActivity;
import com.example.vivi.letsgo.util.UILPauseOnScrollListener;
import com.example.vivi.letsgo.util.UILImageLoader;
import com.baoyz.actionsheet.ActionSheet;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;

public class AddGroupMessageActivity extends BackToolBarActivity {

    private EditText groupMessageContentEdit;
    private RequestQueue mQueue;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    private List<PhotoInfo> mPhotoList;
    private PhotoInfoAdapter mPhotoInfoAdapter;
    private GridView mLvPhoto;
    private FunctionConfig functionConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_message);

        mQueue = Volley.newRequestQueue(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        groupMessageContentEdit = (EditText) findViewById(R.id.edit_group_message_content);

        mLvPhoto = (GridView) findViewById(R.id.lv_photo);
        mPhotoList = new ArrayList<>();
        PhotoInfo photoInfo = new PhotoInfo();
        photoInfo.setPhotoPath(ImageDownloader.Scheme.DRAWABLE.wrap("R.drawable.image_add_photo"));
        mPhotoList.add(photoInfo);
        mPhotoInfoAdapter = new PhotoInfoAdapter(this, mPhotoList);
        mLvPhoto.setAdapter(mPhotoInfoAdapter);
        mLvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActionSheet.createBuilder(AddGroupMessageActivity.this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("相册", "拍照")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                switch (index) {
                                    case 0:
                                        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                        break;
                                    case 1:
                                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        initGalleryFinal();
        initImageLoader(this);
        initFresco();
        x.Ext.init(getApplication());
    }

    private void initGalleryFinal() {
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(51, 153, 51))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.rgb(51, 153, 51))
                .setFabPressedColor(Color.rgb(0, 128, 0))
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.rgb(51, 153, 51))
                .setIconBack(R.mipmap.menu_back)
                .setIconCamera(R.mipmap.menu_add_photo)
                .build();
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = new UILPauseOnScrollListener(false, true);
        imageLoader = new UILImageLoader();

        functionConfigBuilder.setMutiSelectMaxSize(9);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);

        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();


        CoreConfig coreConfig = new CoreConfig.Builder(AddGroupMessageActivity.this, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                mPhotoInfoAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(AddGroupMessageActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(this, config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_group_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_add) {
            ArrayList<MessageImage> imageArrayList = new ArrayList<>();
            for (PhotoInfo photoInfo : mPhotoList){
                imageArrayList.add(decodeImage(photoInfo.getPhotoPath()));
            }
            Intent intent = getIntent();
            mQueue.add(getAddGroupMessageRequest(pref.getString("userid", ""),intent.getStringExtra("groupid"),intent.getStringExtra("groupname"),groupMessageContentEdit.getText().toString(),imageArrayList));
        }
        return super.onOptionsItemSelected(item);
    }

    private MessageImage decodeImage(String path) {
        Bitmap image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(path);
            image = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Toast.makeText(AddGroupMessageActivity.this, "NotFound", Toast.LENGTH_SHORT).show();
            return null;
        }
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        String result = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new MessageImage(String.valueOf(System.currentTimeMillis()),result);
    }

    private StringRequest getAddGroupMessageRequest(final String userid,final String groupid,final String groupname,final String content, final ArrayList<MessageImage> imageArrayList){
        String fileNames;
        String imgData;
        if (imageArrayList.size() != 0){
            fileNames = imageArrayList.get(0).getName();
            imgData = imageArrayList.get(0).getData();
            for (int i = 1; i < imageArrayList.size(); i++) {
                fileNames = fileNames + ";" + imageArrayList.get(i).getName();
                imgData = imgData + ";" + imageArrayList.get(i).getData();
            }
        }
        else {
            fileNames = "";
            imgData = "";
        }
        final String filenames = fileNames;
        final String imgdata = imgData;
        StringRequest addGroupMessageRequest = new StringRequest(Request.Method.POST,"http://letsgo966.vipsinaapp.com/index.php/home/clientapp/addpost1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putBoolean("needRefresh",true);
                        editor.commit();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddGroupMessageActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("userid",userid);
                map.put("groupid",groupid);
                map.put("groupname",groupname);
                map.put("content",content);
                map.put("imgcnt",String.valueOf(imageArrayList.size()));
                map.put("filenames",filenames);
                map.put("imgdata",imgdata);
                return map;
            }
        };
        return addGroupMessageRequest;
    }
}
