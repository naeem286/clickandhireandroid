package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/14/2016.
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

public class AdapterBid extends RecyclerView.Adapter<AdapterBid.RecyclerViewHolder> {
    String[] name, bid;
    private Activity activity;
    public ImageLoader imageLoader;
    private List<ModelBid> arrayList= new ArrayList<ModelBid>();

    public AdapterBid(Activity a, List<ModelBid> arrayList)
    {
        activity = a;
        this.arrayList=arrayList;
        imageLoader = new ImageLoader(activity.getApplicationContext());

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_list_row,parent,false);
        RecyclerViewHolder recyclerViewHolder= new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ModelBid dataProvider= arrayList.get(position);
        String urlForImage = AppConfig.URL_For_Image + dataProvider.getuId()+".jpg";
        imageLoader.DisplayImage(urlForImage, holder.image);
        holder.tx_name.setText(dataProvider.getName());
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
        TextView tx_name, tx_project_bid, tx_project_date;
        public RecyclerViewHolder(View view)
        {
            super(view);
            image= (ImageView)view.findViewById(R.id.image);
            tx_name= (TextView)view.findViewById(R.id.name);
            tx_project_bid= (TextView)view.findViewById(R.id.bid);
            tx_project_date= (TextView)view.findViewById(R.id.date);
        }
    }
}



