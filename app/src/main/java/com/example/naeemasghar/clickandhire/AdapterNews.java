package com.example.naeemasghar.clickandhire;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/12/2016.
 */
public class AdapterNews extends RecyclerView.Adapter<AdapterNews.MyViewHolder> {

    private Activity activity;
    private List<ModelNews> arrayList= new ArrayList<ModelNews>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, detail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.detail);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public AdapterNews(List<ModelNews> arrayList)
    {
        this.arrayList=arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModelNews news = arrayList.get(position);
        holder.title.setText(news.getTitle());
        holder.detail.setText(news.getDetail());
        holder.date.setText(news.getDate().substring(0, Math.min(news.getDate().length(), 10)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
