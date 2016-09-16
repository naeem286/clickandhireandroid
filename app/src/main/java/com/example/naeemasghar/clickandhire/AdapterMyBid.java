package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 8/21/2016.
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/12/2016.
 */

public class AdapterMyBid extends RecyclerView.Adapter<AdapterMyBid.RecyclerViewHolder> {
    String[] title, bid;
    private Activity activity;
    private List<ModelBid> arrayList= new ArrayList<ModelBid>();

    public AdapterMyBid(List<ModelBid> arrayList)
    {
        this.arrayList=arrayList;

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bid_list_row,parent,false);
        RecyclerViewHolder recyclerViewHolder= new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ModelBid dataProvider= arrayList.get(position);
        holder.tx_title.setText(dataProvider.getName());
        holder.tx_project_bid.setText(dataProvider.getUser_bid());
        holder.tx_project_date.setText(dataProvider.getDate().substring(0, Math.min(dataProvider.getDate().length(), 10)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView tx_title, tx_project_bid, tx_project_date;
        public RecyclerViewHolder(View view)
        {
            super(view);
            tx_title= (TextView)view.findViewById(R.id.title);
            tx_project_bid= (TextView)view.findViewById(R.id.bid);
            tx_project_date= (TextView)view.findViewById(R.id.date);
        }
    }
}



