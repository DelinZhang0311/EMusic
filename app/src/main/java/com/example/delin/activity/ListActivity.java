package com.example.delin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.delin.adapter.MusicAdapter;
import com.example.delin.bean.MusicData;
import com.example.delin.util.ReaderMusic;

import java.util.List;

/**
 * Created by delin on 2016/6/27.
 */
public class ListActivity extends Activity {

    private ListView mListView;
    private List<MusicData> listDatas;
    private ImageView back;
    private MusicAdapter mAdapter;
    int a = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        mListView = (ListView) findViewById(R.id.list_music);
        back = (ImageView) findViewById(R.id.back);


        listDatas = ReaderMusic.getInstance(this).readerLocalMusic();
        if (listDatas != null) {
            mAdapter = new MusicAdapter(this, listDatas);
            mListView.setAdapter(mAdapter);
        } else {
//            listDatas = ReaderMusic.getInstance(this).readerLocalMusic();
//            mAdapter = new MusicAdapter(this,listDatas);
//            mListView.setAdapter(mAdapter);
            Toast.makeText(this, "没有扫描到本地音频文件！", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicData data = listDatas.get(position);
                String mMusic = data.getUrl();
                String name = data.getTitle();
                String artist = data.getArtist();

                Intent intent = new Intent();

//                pref= getSharedPreferences("data",MODE_PRIVATE);
//                editor=pref.edit();
//
//                editor.putString("name",name);
//                editor.putString("artist",artist);
//                editor.putInt("position",0);
//                editor.commit();
                intent.putExtra("id", position);
//                intent.putExtra("mStop",1);
//                intent.putExtra("Music",mMusic);
//                intent.putExtra("Name",name);
//                intent.putExtra("Artist",artist);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
