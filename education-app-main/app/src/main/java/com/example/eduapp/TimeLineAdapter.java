package com.example.eduapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TimeLineAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Post> postList;

    private Consumer<String> callback = new Consumer<String>() {
        @Override
        public void accept(String s) {

        }
    };

    public TimeLineAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return postList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.postrow,parent,false);

        ((TextView)convertView.findViewById(R.id.date)).setText(postList.get(position).getDate());
        ((TextView)convertView.findViewById(R.id.body)).setText(postList.get(position).getBody());

        Button know = convertView.findViewById(R.id.know);
        know.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(postList.get(position).getCategory());
                        callback.accept(postList.get(position).getCategory());
                    }
                }
        );

        return convertView;
    }

    public void setOnKnownCategoryButtonClickListener(Consumer<String> callback){
        this.callback = callback;
    }
}
