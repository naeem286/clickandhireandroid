package com.example.naeemasghar.clickandhire;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/29/2016.
 */
public class AdapterRating extends RecyclerView.Adapter<AdapterRating.RecyclerViewHolder> {
    String[] name, rating;
    //private ArrayList<ModelRating> arrayList= new ArrayList<ModelRating>();
    private List<ModelRating> arrayList = new ArrayList<ModelRating>();
    private Activity activity;
    public ImageLoader imageLoader;


    public AdapterRating(Activity a, List<ModelRating> arrayList)
    {
        activity = a;
        this.arrayList=arrayList;
        imageLoader = new ImageLoader(activity.getApplicationContext());

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_list_row,parent,false);
        RecyclerViewHolder recyclerViewHolder= new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ModelRating dataProvider= arrayList.get(position);
        String urlForImage = AppConfig.URL_For_Image + dataProvider.getuId()+".jpg";
        imageLoader.DisplayImage(urlForImage, holder.image);
        //holder.image.setImageResource(dataProvider.getuId());

        holder.tx_name.setText(dataProvider.getName());
        holder.ratingBar.setNumStars(Integer.parseInt(dataProvider.getStars()));
        holder.tx_user_comment.setText(dataProvider.getComment());
        holder.tx_date.setText(dataProvider.getDate().substring(0, Math.min(dataProvider.getDate().length(), 10)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView tx_name, tx_user_comment, tx_date;
        RatingBar ratingBar;
        public RecyclerViewHolder(View view)
        {
            super(view);
            image= (ImageView)view.findViewById(R.id.image);
            tx_name= (TextView)view.findViewById(R.id.name);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            tx_user_comment= (TextView)view.findViewById(R.id.comment);
            tx_date= (TextView)view.findViewById(R.id.date);
        }
    }
}

