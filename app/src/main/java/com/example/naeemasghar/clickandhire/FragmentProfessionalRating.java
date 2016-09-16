package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Naeem Asghar on 8/1/2016.
 */
public class FragmentProfessionalRating extends Fragment {
    private static final String TAG = ActivityProfessionalDetail.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] name, rating;
    public List<ModelRating> RatingList;
    private ArrayList<Rating> arrayList= new ArrayList<Rating>();

    public FragmentProfessionalRating() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.professional_fragment_rating, container, false);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);

        String id =getActivity().getIntent().getStringExtra("id");
        getRatingByUserID(id);

        name=getResources().getStringArray(R.array.professional_name);
        rating=getResources().getStringArray(R.array.user_rating);
        RatingList = new ArrayList<ModelRating>();
        return v;
    }


    /**
     * function to verify login details in mysql db
     * */
    private void getRatingByUserID(final String Id){
        class GetUserRating extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getRatings(s);
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(), "Rating is loaded Successfully", Toast.LENGTH_LONG).show();
                viewRatingList();
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",Id);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_GET_RATING_BY_USER_ID,hashMap);
                return s;
            }
        }

        GetUserRating cul = new GetUserRating();
        cul.execute();
    }

    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getRatings(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Rating List No Error ");
                JSONArray result = jObj.getJSONArray("ratingList");
                for(int i = 0; i<result.length(); i++){
                    ModelRating obj = new ModelRating();

                    JSONObject rating = result.getJSONObject(i);
                    String id = rating.getString("ur_id");
                    String name = rating.getString("user");
                    String rated_user_id = rating.getString("rated_user_id");
                    String rated_user = rating.getString("rated_user");
                    String stars = rating.getString("rt_stars");
                    String avg_rating = rating.getString("avg_rating");
                    String ratings = rating.getString("ratings");
                    String rt_comment = rating.getString("rt_comment");
                    String created_at = rating.getString("created_at");

                    obj.setuId(id);
                    obj.setName(name);
                    obj.setRuId(rated_user_id);
                    obj.setStars(stars);
                    obj.setAvtRating(avg_rating);
                    obj.setComment(rt_comment);
                    obj.setDate(created_at);

                    Log.d(TAG, "Get Rating is working");
                    //sendUsersToDB(obj);
                    //professionalList = new ArrayList<ModelUser>();
                    RatingList.add(obj);

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

    //SHOW DATA IN CUSTOMIZE LIST
    public void viewRatingList()
    {
        Log.d(TAG, "View Online List Method send to data Provider ");

        adapter= new AdapterRating(getActivity(),RatingList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }



}
