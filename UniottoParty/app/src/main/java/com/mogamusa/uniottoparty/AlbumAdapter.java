package com.mogamusa.uniottoparty;

/**
 * Created by masayuki on 2017/09/28.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by masayuki on 2017/09/25.
 */

public class AlbumAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Album> albumList;

    public AlbumAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return albumList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.album_row,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(albumList.get(position).getName());
        ((TextView)convertView.findViewById(R.id.tweet)).setText(albumList.get(position).getArtist());

        return convertView;
    }
}
