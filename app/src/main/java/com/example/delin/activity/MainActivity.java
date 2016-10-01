package com.example.delin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delin.bean.MusicData;
import com.example.delin.util.ReaderMusic;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private ImageView menu, lastmusic, nextmusic, pause, listmusic;
    private TextView txt_musicName, txt_musicArtist, current_time, end_time;
    private SeekBar seekBar;
    private List<MusicData> listDatas;

    private static boolean isExit = false;

    private SharedPreferences pref;

    private SensorManager sensorManager;//管理传感器，实现摇一摇切歌

    boolean isChanging = false;
    int position;
    int mStop = 0;//停止标志
    int mMode;//播放模式
    int mShake;//摇一摇开关
    int time1;
    int time2;
//    String musicName;   //歌名
//    String musicArtist; //艺术家

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        init_Rack();//主界面初始化处理
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());

        listDatas = ReaderMusic.getInstance(this).readerLocalMusic();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);


        pref = getSharedPreferences("data", MODE_PRIVATE);
        mMode = pref.getInt("mMode", 0);
        mShake = pref.getInt("mShake", 0);


//        musicName=pref.getString("name","无");
//        musicArtist=pref.getString("artist","未知");
//        position=pref.getInt("position",0);

//        Intent intent = getIntent();
//        mStop=intent.getIntExtra("mStop",0);
//        if(mStop!=0) {
//            position = intent.getIntExtra("id", 1);
//            //mediaPlayer.reset();
//            MusicPlay(position);
//        }
        //Toast.makeText(MainActivity.this,""+mMode,Toast.LENGTH_SHORT).show();


        /**
         * 音乐列表监听器
         */
        listmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listview(view);
            }
        });


        /**
         * 下一首歌曲监听器
         */
        nextmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMusic(view);
            }
        });

        /**
         * 上一首歌曲监听器
         */
        lastmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LastMusic(view);
            }
        });

        /**
         * 暂停、播放监听器
         */
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Start(view);
            }
        });


        /**
         * 播放模式监听器
         */
//        mode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MusicMode(view);
//            }
//        });

        /**
         * 歌曲播放完监听
         */

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mMode == 0) {//顺序播放
                    position++;
                    if (position >= listDatas.size()) {
                        position = 0;
                    }
                    MusicPlay(position);

                } else if (mMode == 1) {//随机播放

                    position = (int) (Math.random() * listDatas.size());
                    MusicPlay(position);

                } else if (mMode == 2) {//单曲循环
                    MusicPlay(position);
                }
            }
        });

        /**
         * Error监听
         */
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                mediaPlayer.stop();

                return false;
            }
        });


        /**
         * prepaer监听
         */
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mhandler.sendEmptyMessage(0);
                mhandler.sendEmptyMessageDelayed(1, 1000);
                mediaPlayer.start();
            }
        });

        /**
         * 菜单点击事件
         */
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mhandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMode = pref.getInt("mMode", 0);
        mShake = pref.getInt("mShake", 0);
//        musicName=pref.getString("name","无");
//        musicArtist=pref.getString("artist","未知");
//        position=pref.getInt("position",0);
//        MusicPlay(position);
//        Toast.makeText(MainActivity.this,""+mMode,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    /**
     * 实现摇一摇切歌功能
     */
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (mShake == 1) {
                float xValue = Math.abs(sensorEvent.values[0]);
                float yValue = Math.abs(sensorEvent.values[1]);
                float zValue = Math.abs(sensorEvent.values[2]);
                if (xValue > 13 || yValue > 13 || zValue > 13) {
                    switch (mMode) {
                        case 1:
                            position = (int) (Math.random() * listDatas.size());
                            MusicPlay(position);
                            break;
                        case 0:
                            position += 1;
                            if (position >= listDatas.size()) {
                                position = 0;
                            }
                            MusicPlay(position);
                            break;
                        case 2:
                            position += 1;
                            if (position >= listDatas.size()) {
                                position = 0;
                            }
                            MusicPlay(position);
                            break;
                    }
                    pause.setImageResource(R.drawable.pause);
                    Toast.makeText(MainActivity.this, "您已摇动手机，将自动为您切换为下一首歌曲", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    /**
     * 音轨监听器
     */
    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
                current_time.setText(getFileTime(progress));
            }

        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            //mediaPlayer.seekTo(seekBar.getProgress());
            isChanging = false;
        }

    }

    /**
     * 新线程调节seekBar
     */
    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
            if (msg.what == 0) {

            } else if (msg.what == 1) {
                seekBar.setMax(mediaPlayer.getDuration());//设置进度条
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                time1 = mediaPlayer.getDuration();
                time2 = mediaPlayer.getCurrentPosition();

                current_time.setText(getFileTime(time2));
                end_time.setText(getFileTime(time1));

                mhandler.sendEmptyMessageDelayed(1, 1000);
            }
        }

    };


    /**
     * 主界面初始化处理
     */
    private void init_Rack() {
        mediaPlayer = new MediaPlayer();
        menu = (ImageView) findViewById(R.id.menu);
        lastmusic = (ImageView) findViewById(R.id.before_music);
        nextmusic = (ImageView) findViewById(R.id.next_music);
        pause = (ImageView) findViewById(R.id.pause_music);
        listmusic = (ImageView) findViewById(R.id.img_musiclist);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        txt_musicName = (TextView) findViewById(R.id.current_music);
        txt_musicArtist = (TextView) findViewById(R.id.music_artist);
        current_time = (TextView) findViewById(R.id.current_time);
        end_time = (TextView) findViewById(R.id.end_time);
    }


    public void Start(View v) {

        switch (mStop) {
            case 0:
                break;
            case 1:
                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                } else {
                    pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
                break;

        }
    }


    /**
     * 播放列表
     *
     * @param v
     */
    public void Listview(View v) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            position = data.getIntExtra("id", 1);
//            musicName = data.getStringExtra("Name");
//            musicArtist = data.getStringExtra("Artist");


            MusicPlay(position);
            pause.setImageResource(R.drawable.pause);
            mStop = 1;
        }
    }


    //下一首
    public void NextMusic(View v) {
        mStop = 1;

        switch (mMode) {
            case 1:
                position = (int) (Math.random() * listDatas.size());
                MusicPlay(position);
                break;
            case 0:
                position += 1;
                if (position >= listDatas.size()) {
                    position = 0;
                }
                MusicPlay(position);
                break;
            case 2:
                position += 1;
                if (position >= listDatas.size()) {
                    position = 0;
                }
                MusicPlay(position);
                break;
        }
        pause.setImageResource(R.drawable.pause);
    }

    //上一首
    public void LastMusic(View v) {
        mStop = 1;
        if (position < 1) {
            position = listDatas.size() - 1;
            MusicPlay(position);
        } else {
            position -= 1;
            if (position < 0) {
                position = listDatas.size();
            }
            MusicPlay(position);
            pause.setImageResource(R.drawable.pause);
        }
    }

    //播放模式
//    public void MusicMode(View v){
//
//        switch (mMode){
//            case 0:
//                mode.setImageResource(R.drawable.random_play);
//                mMode = mMode + 1;
//                break;
//            case 1:
//                mode.setImageResource(R.drawable.single_play);
//                mMode = mMode + 1;
//                break;
//            case 2:
//                mode.setImageResource(R.drawable.order_play);
//                mMode = mMode - 2;
//                break;
//        }
//    }


    /**
     * 播放音乐
     *
     * @param mpostion
     */
    private void MusicPlay(int mpostion) {
        MusicData data = listDatas.get(mpostion);
        String mUrl = data.getUrl();
        txt_musicName.setText(data.getTitle());
        if (data.getArtist().equals("<unknown>")) {
            txt_musicArtist.setText("未知");
        } else {
            txt_musicArtist.setText(data.getArtist());
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mUrl);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    /**
     * 获取音乐文件的播放持续时间长的格式化字符串
     *
     * @param timeMs
     * @return 格式化时间字符串
     */
    private String getFileTime(int timeMs) {
        int totalSeconds = timeMs / 1000;// 获取文件有多少秒
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale
                .getDefault());
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        //int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);

        return mFormatter.format("%02d:%02d", minutes, seconds)
                .toString();// 格式化字符串
    }
}
