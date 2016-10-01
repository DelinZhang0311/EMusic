package com.example.delin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by DeLin on 2016/6/29.
 */
public class MenuActivity extends Activity {

    private LinearLayout playMode;
    private LinearLayout shake;
    //private LinearLayout musicList;
    private LinearLayout about;
    private ImageView back;
    private ImageView imgMode;
    private ImageView imgShake;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private int mMode;
    private int mShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        playMode = (LinearLayout) findViewById(R.id.lea_mode);
        shake = (LinearLayout) findViewById(R.id.lea_shake);
        about = (LinearLayout) findViewById(R.id.lea_about);
        back = (ImageView) findViewById(R.id.back);
        imgMode = (ImageView) findViewById(R.id.img_mode);
        imgShake = (ImageView) findViewById(R.id.img_shake);
        //musicList=(LinearLayout)findViewById(R.id.lea_list);


        /**
         * 从SharedPreferes中取出播放模式标志，判断并设置播放模式图标
         */
        pref = getSharedPreferences("data", MODE_PRIVATE);
        editor = pref.edit();
        mMode = pref.getInt("mMode", 0);
        switch (mMode) {
            case 0:
                imgMode.setImageResource(R.drawable.order_play);
                break;
            case 1:
                imgMode.setImageResource(R.drawable.random_play);
                break;
            case 2:
                imgMode.setImageResource(R.drawable.single_play);
                break;
        }


        /**
         * 从SharedPreferes中取出摇一摇切歌标志，判断并设置开关
         */
        mShake = pref.getInt("mShake", 0);
        switch (mShake) {
            case 0:
                imgShake.setImageResource(R.drawable.ad2);
                break;
            case 1:
                imgShake.setImageResource(R.drawable.ad3);
                break;
        }


        /**
         * 播放模式点击事件
         */
        playMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast;
                switch (mMode) {
                    case 0:
                        mMode = mMode + 1;
                        imgMode.setImageResource(R.drawable.random_play);
                        editor.putInt("mMode", mMode);
                        editor.commit();
                        toast = Toast.makeText(MenuActivity.this, "随机播放", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    case 1:
                        mMode = mMode + 1;
                        imgMode.setImageResource(R.drawable.single_play);
                        editor.putInt("mMode", mMode);
                        editor.commit();
                        toast = Toast.makeText(MenuActivity.this, "单曲循环", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    case 2:
                        mMode = mMode - 2;
                        imgMode.setImageResource(R.drawable.order_play);
                        editor.putInt("mMode", mMode);
                        editor.commit();
                        toast = Toast.makeText(MenuActivity.this, "顺序播放", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                }
            }
        });


        /**
         * 摇一摇切歌点击事件
         */
        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mShake) {
                    case 0:
                        mShake = mShake + 1;
                        imgShake.setImageResource(R.drawable.ad3);
                        editor.putInt("mShake", mShake);
                        editor.commit();
                        break;
                    case 1:
                        mShake = mShake - 1;
                        imgShake.setImageResource(R.drawable.ad2);
                        editor.putInt("mShake", mShake);
                        editor.commit();
                        break;
                }
            }
        });


        /**
         * 音乐列表点击事件
         */
//        musicList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MenuActivity.this,ListActivity.class);
//                startActivity(intent);
//                //finish();
//            }
//        });


        /**
         * 关于我们点击事件
         */
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
