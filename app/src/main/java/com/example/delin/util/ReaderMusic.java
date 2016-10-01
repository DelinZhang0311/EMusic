package com.example.delin.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.delin.bean.MusicData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delin on 2016/6/26.
 */
public class ReaderMusic {

    private static ReaderMusic instance;

    private Cursor mCursor;
    private Context mContext;
    private List<MusicData> listDatas, listDatas1;


    private ReaderMusic(Context context) {
        this.mCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        mContext = context;
        Log.i("123456", "~~~~~~ -------------");

    }

    public static ReaderMusic getInstance(Context context) {
        if (instance == null) {
            synchronized (ReaderMusic.class) {
                if (instance == null) {
                    instance = new ReaderMusic(context);
                }
            }
        }
        return instance;
    }

    /**
     * 读取本地音乐文件
     */
    public List<MusicData> readerLocalMusic() {
        if (mCursor != null && mCursor.getCount() > 0) {
            listDatas = new ArrayList<>();
            while (mCursor.moveToNext()) {
                //音乐id
                long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                //音乐标题
                String title = mCursor.getString((mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                //艺术家
                String artist = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                //时长
                long duration = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                //文件大小
                long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                //文件路径
                String url = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //是否为音乐
                int isMusic = mCursor.getInt(mCursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                //如果为音乐文件则添加到数据列表
                if (isMusic != 0) {
                    MusicData data = new MusicData();
                    data.setId(id);
                    data.setTitle(title);
                    data.setArtist(artist);
                    data.setDuration(duration);
                    data.setSize(size);
                    data.setUrl(url);
                    listDatas.add(data);
                }
            }
//            toast("size--"+listDatas.size());

            if (listDatas.size() == 0) {
                listDatas1 = null;
            } else {
                listDatas1 = listDatas;
            }
            return listDatas1;
        } else {
            debug("没有读取到本地音频文件");
            return null;
        }
    }


    private void debug(String str) {
        Log.d(ReaderMusic.class.getSimpleName(), str);
    }

}


