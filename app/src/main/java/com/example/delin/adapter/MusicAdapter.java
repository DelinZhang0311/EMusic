package com.example.delin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.delin.activity.R;
import com.example.delin.bean.MusicData;

import java.util.List;

/**
 * Created by delin on 2016/6/26.
 */
public class MusicAdapter extends BaseAdapter {

    private List<MusicData> listDatas;
    private LayoutInflater inflater;

    public MusicAdapter(Context context, List<MusicData> list) {
        this.listDatas = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return listDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MusicData data = listDatas.get(i);
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, null);

            holder.musicName = (TextView) view.findViewById(R.id.txt_name);
            holder.musicArtist = (TextView) view.findViewById(R.id.txt_artist);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.musicName.setText(data.getTitle()+"");
        holder.musicArtist.setText(data.getArtist()+"");


        return view;
    }

    private static class ViewHolder {
        TextView musicName;
        TextView musicArtist;
    }

}
