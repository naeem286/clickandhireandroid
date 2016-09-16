package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 8/1/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class FragmentWorkProposals extends Fragment {
    private static final String TAG = ActivityWorkDetail.class.getSimpleName();
    private List<ModelBid> bidList = new ArrayList<>();
    private TextView txtAwardBid;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] name, bid;
    private ArrayList<Bid> arrayList= new ArrayList<Bid>();
    public String awardedBidUserId,awardedBidUserName;
    public FragmentWorkProposals() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.work_fragment_proposals, container, false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);
        txtAwardBid=(TextView)v.findViewById(R.id.awardBid);
        name=getResources().getStringArray(R.array.professional_name);
        bid=getResources().getStringArray(R.array.project_name);
        String wid=getActivity().getIntent().getStringExtra("wid");
        awardedBidUserId=getActivity().getIntent().getStringExtra("awardBid");
        awardedBidUserName=getActivity().getIntent().getStringExtra("awardName");
        if(awardedBidUserId.equals("0"))
        {
            txtAwardBid.setText("Bid is not yet Awarded");
        }else
        {
            txtAwardBid.setText("Bid is Awarded to "+ awardedBidUserName);
        }
        getBidByTask(wid);

        adapter= new AdapterBid(getActivity(),bidList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void getBidByTask(final String taskId){
        class GetTaskBid extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getBids(s);
                loading.dismiss();
                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                Log.d(TAG, "Bid list json " + s);
                //Toast.makeText(TaskDetailActivity.this,"Bids are downloaded Successfully"+s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("task_id",taskId);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_GET_BIDS_OF_TASK ,hashMap);

                return s;
            }
        }

        GetTaskBid cul = new GetTaskBid();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getBids(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getApplicationContext(),
            //      json, Toast.LENGTH_LONG).show();
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Bids List No Error ");
                ModelBid obj;
                bidList.clear();
                JSONArray result = jObj.getJSONArray("bidsList");
                for(int i = 0; i<result.length(); i++){

                    obj = new ModelBid();
                    JSONObject objBid = result.getJSONObject(i);
                    String bid_id = objBid.getString("b_id");
                    String user_id = objBid.getString("user_id");
                    String task_id = objBid.getString("tk_id");
                    String user_name = objBid.getString("user");
                    String user_bid = objBid.getString("bi_bid");
                    String created_at = objBid.getString("created_at");

                    obj.setbId(bid_id);
                    obj.setuId(user_id);
                    obj.settId(task_id);
                    obj.setName(user_name);
                    obj.setUser_bid(user_bid);
                    obj.setDate(created_at);

                    Log.d(TAG, "Get User is working");
                    bidList.add(obj);
                    adapter.notifyDataSetChanged();
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getActivity(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}


