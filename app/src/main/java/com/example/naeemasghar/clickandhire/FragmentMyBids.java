package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Naeem Asghar on 8/12/2016.
 */
public class FragmentMyBids extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<ModelBid> bidList = new ArrayList<>();
    private List<ModelTask> workList = new ArrayList<>();
    private TextView txtAwardBid;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] name, bid;
    private SQLiteHandler db;
    public String awardedBidUserId,awardedBidUserName;
    private ArrayList<Bid> arrayList= new ArrayList<Bid>();
    private String UserId;
    private String WorkId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_bids, container, false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);
        txtAwardBid=(TextView)v.findViewById(R.id.awardBid);
        name=getResources().getStringArray(R.array.professional_name);
        bid=getResources().getStringArray(R.array.project_name);
        String wid=getActivity().getIntent().getStringExtra("wid");
        awardedBidUserId=getActivity().getIntent().getStringExtra("awardBid");
        awardedBidUserName=getActivity().getIntent().getStringExtra("awardName");
        db = new SQLiteHandler(getActivity());
        final HashMap<String, String> user = db.getUserDetails();
        UserId = user.get("id");
        getBidByUserId(UserId);

        adapter= new AdapterMyBid(bidList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new ActivityWorkList.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ActivityWorkList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelBid bid = bidList.get(position);
                WorkId= bid.gettId();
                getTaskByTaskId(WorkId);
                /*ModelTask work = workList.get(position);
                Intent intent = new Intent(getActivity(), ActivityWorkDetail.class);
                intent.putExtra("uid", UserId);
                intent.putExtra("wid", work.getId());
                intent.putExtra("title", work.getTitle());
                intent.putExtra("detail", work.getDetail());
                intent.putExtra("fee", work.getFees());
                intent.putExtra("city", work.getCity());
                intent.putExtra("awardBid", work.getAwarded_bid());
                intent.putExtra("awardName", work.getAwarded_name());
                intent.putExtra("date", work.getDate());
                startActivity(intent);*/
            }
            @Override
            public void onLongClick(View view, int position) {


            }
        }));


        return v;
    }

    private void getBidByUserId(final String userId){
        class GetBidsByUserId extends AsyncTask<Void,Void,String> {
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
                hashMap.put("user_id",userId);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_GET_BIDS_BY_USER_ID ,hashMap);

                return s;
            }
        }

        GetBidsByUserId cul = new GetBidsByUserId();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getBids(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getActivity().getApplicationContext(),json, Toast.LENGTH_LONG).show();
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
                    String task_title = objBid.getString("tk_title");
                    String user_bid = objBid.getString("bi_bid");
                    String created_at = objBid.getString("created_at");

                    obj.setbId(bid_id);
                    obj.setuId(user_id);
                    obj.settId(task_id);
                    obj.setName(task_title);
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

    // Method to make request to server
    private void getTaskByTaskId(final String taskId){
        class GetUserTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getTasks(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(), "Search is done Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("task_id",taskId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_BY_TASK_ID ,hashMap);
                return s;
            }
        }

        GetUserTasks cul = new GetUserTasks();
        cul.execute();
    }
    private void getTasks(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getApplicationContext(),
            //      json, Toast.LENGTH_LONG).show();
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Task List No Error ");
                workList.clear();
                ModelTask obj;
                JSONArray result = jObj.getJSONArray("taskList");
                //for(int i = 0; i<result.length(); i++){
                    obj= new ModelTask();
                    JSONObject task1 = result.getJSONObject(0);
                    String id = task1.getString("tid");
                    JSONObject user = task1.getJSONObject("task");
                    String user_id = user.getString("user_id");
                    String cat_id = user.getString("cat_id");
                    String user_name = user.getString("user");
                    String title = user.getString("title");
                    String detail = user.getString("detail");
                    String fees = user.getString("fees");
                    String city = user.getString("city");
                    String awardedBid= user.getString("award_bid");
                    String awardedName= user.getString("award_name");
                    String status = user.getString("status");
                    String created_at = user.getString("created_at");

                Intent intent = new Intent(getActivity(), ActivityWorkDetail.class);
                intent.putExtra("uid", UserId);
                intent.putExtra("wid", WorkId);
                intent.putExtra("title", title);
                intent.putExtra("detail", detail);
                intent.putExtra("fee", fees);
                intent.putExtra("city", city);
                intent.putExtra("awardBid", awardedBid);
                intent.putExtra("awardName", awardedName);
                intent.putExtra("date", created_at);
                startActivity(intent);


                    Log.d(TAG, "Get User is working");

               // }

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