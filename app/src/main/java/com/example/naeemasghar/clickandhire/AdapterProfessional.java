package com.example.naeemasghar.clickandhire;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/12/2016.
 */
public class AdapterProfessional extends RecyclerView.Adapter<AdapterProfessional.RecyclerViewHolder> {
    String[] name, bids;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Activity activity;
    public ImageLoader imageLoader;
    private List<ModelUser> arrayList= new ArrayList<ModelUser>();
    public AdapterProfessional(Activity a, List<ModelUser> arrayList)
    {
        activity = a;
        this.arrayList=arrayList;
        imageLoader = new ImageLoader(activity.getApplicationContext());

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row,parent,false);
        RecyclerViewHolder recyclerViewHolder= new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ModelUser dataProvider= arrayList.get(position);
        Log.d(TAG,"Before send to Adaptor Users list" + dataProvider.getId() + dataProvider.getName() + dataProvider.getSpeciality() + dataProvider.getFees());
        //new DownloadImage(holder.image).execute(dataProvider.getProfessional_ID());
        //holder.image.setImageResource(dataProvider.getProfessional_ID());
        //DisplayImage function from ImageLoader Class
        String urlForImage = AppConfig.URL_For_Image + dataProvider.getId()+".jpg";
        imageLoader.DisplayImage(urlForImage, holder.image);
        holder.tx_name.setText(dataProvider.getName());
        holder.tx_speciality.setText(dataProvider.getSpeciality());
        holder.tx_fee.setText(dataProvider.getFees()+" per Hour");
        holder.ratingBar.setRating(Math.round(Float.parseFloat(dataProvider.getAvtRating())));
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView tx_name, tx_speciality,tx_fee;
        RatingBar ratingBar;
        public RecyclerViewHolder(View view)
        {
            super(view);
            image= (ImageView)view.findViewById(R.id.image);
            tx_name= (TextView)view.findViewById(R.id.name);
            tx_speciality= (TextView)view.findViewById(R.id.speciality);
            tx_fee= (TextView)view.findViewById(R.id.fee);
            ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        }
    }
}


