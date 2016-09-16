package com.example.naeemasghar.clickandhire;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class AdapterWork extends RecyclerView.Adapter<AdapterWork.MyViewHolder> {

    private List<ModelTask> workList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, detail, fee;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.detail);
            date = (TextView) view.findViewById(R.id.date);
            fee = (TextView) view.findViewById(R.id.fee);
        }
    }


    public AdapterWork(List<ModelTask> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModelTask work = workList.get(position);
        holder.title.setText(work.getTitle());
        String subDetail = work.getDetail().substring(0, Math.min(work.getDetail().length(), 42));
        holder.detail.setText(subDetail+"...");
        holder.date.setText(work.getDate().substring(0, Math.min(work.getDate().length(), 10)));
        holder.fee.setText(work.getFees()+" PKR");
    }

    @Override
    public int getItemCount() {
        return workList.size();
    }
}
