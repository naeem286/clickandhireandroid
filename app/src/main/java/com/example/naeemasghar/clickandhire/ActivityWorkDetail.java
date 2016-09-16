package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityWorkDetail extends AppCompatActivity {
    private static final String TAG = ActivityWorkDetail.class.getSimpleName();
    public TextView title, date, detail;
    private String Uid,Wid,Title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle bundle;
    public String awardedBidUserId,awardedBidUserName;
    private int[] tabIcons = {
            R.drawable.ic_communities,
            R.drawable.ic_people,
            R.drawable.ic_whats_hot
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_work_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Project Detail");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Title=getIntent().getStringExtra("title");
        Uid=getIntent().getStringExtra("uid");
        Wid=getIntent().getStringExtra("wid");
        awardedBidUserId=getIntent().getStringExtra("awardBid");
        awardedBidUserName=getIntent().getStringExtra("awardName");
        //setupTabIcons();

        bundle = new Bundle();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.postBid);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awardedBidUserId.equals("0")) {
                    showChangeLangDialog(Uid, Wid, Title);
                }else
                {
                    Snackbar.make(view, "Bid is Already Awarded to "+awardedBidUserName, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public void showChangeLangDialog(final String userId, final String workId, final String workTitle) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_bid, null);
        dialogBuilder.setView(dialogView);

        final EditText txtBid = (EditText) dialogView.findViewById(R.id.userBid);


        String title= "Bid on "+ workTitle;
        dialogBuilder.setTitle(title);

        dialogBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String userBid = txtBid.getText().toString();

                if(!userBid.isEmpty())
                {
                    postBid(userId, workId, userBid);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please Write Proposal First...!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        // SqLite database handler
    }

    /**
     * Post Bid to sever
     * */
    private void postBid(final String userId, final String taskId, final String userBid){
        class PlaceUserBid extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityWorkDetail.this,"Posting...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //getBid(s);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Bid is Posted Successfully",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",userId);
                hashMap.put("task_id",taskId);
                hashMap.put("bid",userBid);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_PLACE_TASK_BID,hashMap);

                return s;
            }
        }

        PlaceUserBid cul = new PlaceUserBid();
        cul.execute();
    }

    private void getBid(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getApplicationContext(),
            //      json, Toast.LENGTH_LONG).show();
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                //JSONArray result = jObj.getJSONArray("bidsList");
                //for(int i = 0; i<result.length(); i++){
                String bid_id = jObj.getString("b_id");
                String user_id = jObj.getString("user_id");
                String task_id = jObj.getString("tk_id");
                String user_name = jObj.getString("user_name");
                String user_bid = jObj.getString("bi_bid");
                String created_at = jObj.getString("created_at");

                bundle.putString("userId", user_id);
                bundle.putString("taskId", task_id);
                bundle.putString("userName", user_name);
                bundle.putString("bid", user_bid);
                bundle.putString("date", created_at);
                // set Fragmentclass Arguments
                FragmentWorkProposals obj = new FragmentWorkProposals();
                obj.setArguments(bundle);

                // Reload current fragment
                Fragment frg = null;
                frg = getSupportFragmentManager().findFragmentByTag("FragmentWorkProposals");
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();

                //}

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentWorkDetail(), "Detail");
        adapter.addFrag(new FragmentWorkProposals(), "Proposal");
        //adapter.addFrag(new ThreeFragment(), "Place Bid");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


