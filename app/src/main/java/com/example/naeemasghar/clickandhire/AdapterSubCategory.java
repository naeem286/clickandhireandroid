package com.example.naeemasghar.clickandhire;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.MyViewHolder> {

    private ArrayList<String> subCategoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, detail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //detail = (TextView) view.findViewById(R.id.detail);
            //date = (TextView) view.findViewById(R.id.date);
        }
    }


    public AdapterSubCategory(ArrayList<String> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String subCategoryName = subCategoryList.get(position);
        holder.title.setText(subCategoryName);
       // holder.detail.setText("1200 Projects");
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }
}


