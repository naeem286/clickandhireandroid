package com.example.naeemasghar.clickandhire;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RatingBar;
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
public class FragmentMyWorkBids  extends Fragment {
    private static final String TAG = ActivityWorkDetail.class.getSimpleName();
    private List<ModelBid> bidList = new ArrayList<>();
    private TextView txtAwardBid;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] name, bid;
    private SQLiteHandler db;
    public String awardedBidUserId,awardedBidUserName;
    private ArrayList<Bid> arrayList= new ArrayList<Bid>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_work_bids, container, false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);
        txtAwardBid=(TextView)v.findViewById(R.id.awardBid);
        name=getResources().getStringArray(R.array.professional_name);
        bid=getResources().getStringArray(R.array.project_name);
        db = new SQLiteHandler(getActivity());
        String wid=getActivity().getIntent().getStringExtra("wid");
        awardedBidUserId=getActivity().getIntent().getStringExtra("awardBid");
        awardedBidUserName=getActivity().getIntent().getStringExtra("awardName");
        // String strtext = getArguments().getString("userName");
        //Toast.makeText(getActivity(), strtext, Toast.LENGTH_LONG).show();
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

        recyclerView.addOnItemTouchListener(new ActivityWorkList.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ActivityWorkList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelBid bid = bidList.get(position);
                String userId= bid.getuId();
                getUserById(userId);


            }
            @Override
            public void onLongClick(View view, int position) {

                ModelBid bid = bidList.get(position);
                txtAwardBid.setText("Bid is Awarded to "+ bid.getName());
                String taskId= bid.gettId();
                String userId= bid.getuId();
                postBidAwarded(taskId, userId);
            }
        }));

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

    private void postBidAwarded(final String task_id,final String user_id){


        class UpdateAwardBid extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Posting..", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("task_id",task_id);
                hashMap.put("user_id",user_id);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_AWARD_BID,hashMap);

                return s;
            }
        }

        UpdateAwardBid ue = new UpdateAwardBid();
        ue.execute();
    }

    private void getUserById(final String userId){
        class GetUserByName extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getProfessionals(s);
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(), "Search is done Successfully", Toast.LENGTH_LONG).show();
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",userId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_USER_BY_ID,hashMap);
                return s;
            }
        }

        GetUserByName cul = new GetUserByName();
        cul.execute();
    }


    private void getProfessionals(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Cities List No Error ");
                JSONArray result = jObj.getJSONArray("userList");
                for(int i = 0; i<result.length(); i++){
                    ModelUser obj = new ModelUser();

                    JSONObject user1 = result.getJSONObject(i);
                    String id = user1.getString("uid");
                    JSONObject user = user1.getJSONObject("user");
                    String name = user.getString("name");
                    String phone = user.getString("phone");
                    String email = user.getString("email");
                    String tagLine = user.getString("tagLine");
                    String fees = user.getString("fees");
                    String description = user.getString("description");
                    String experience = user.getString("experience");
                    String speciality = user.getString("speciality");
                    String city = user.getString("city");
                    String profileImage = user.getString("profileImage");
                    String apiKey = user.getString("apiKey");
                    String avgRating = user.getString("avg_rating");
                    String totalRatings = user.getString("ratings");
                    String status = user.getString("status");
                    String created_at = user.getString("created_at");

                    String cityName=db.getCityById(city);
                // Launch Login Activity
                Intent intent = new Intent(getActivity(), ActivityProfessionalDetail.class);

                intent.putExtra("ratingBool", "1");
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("city", cityName);
                intent.putExtra("tag_line", tagLine);
                intent.putExtra("summary", description);
                intent.putExtra("speciality", speciality);
                intent.putExtra("experience", experience);
                intent.putExtra("fee", fees);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("rating", avgRating);
                intent.putExtra("totalRating",totalRatings);
                startActivity(intent);

                    Log.d(TAG, "Get User is working");
                    Log.d(TAG, "before sending to  list" + obj.getId() + obj.getName() + obj.getSpeciality() + obj.getFees());
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

