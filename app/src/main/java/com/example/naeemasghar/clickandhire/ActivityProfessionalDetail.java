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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityProfessionalDetail extends AppCompatActivity {

    private SQLiteHandler db;
    public TextView title, date, detail;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RatingBar ratingBar;
    private int[] tabIcons = {
            R.drawable.ic_communities,
            R.drawable.ic_people,
            R.drawable.ic_whats_hot
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_professional_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Professional Detail");
        // SqLite database handler
        db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        final String id = user.get("id");
        final String ratingBool = getIntent().getStringExtra("ratingBool");
        final String ratedUserName = getIntent().getStringExtra("name");
        final String ratedUserId = getIntent().getStringExtra("id");
         final String rattingBool = getIntent().getStringExtra("id");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.postRating);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("ratingBool").equals("0")) {
                    Snackbar.make(view, "You should be registered first to Rate Professional", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    showChangeLangDialog(id, ratedUserId, ratedUserName);
                }
            }
        }
        );


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void showChangeLangDialog(final String userId, final String ratedUserId, final String ratedUserName) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_rating, null);
        dialogBuilder.setView(dialogView);

        final EditText txtComment = (EditText) dialogView.findViewById(R.id.userComment);

        ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);

        String title= "Rate "+ ratedUserName;
        dialogBuilder.setTitle(title);

        dialogBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String comment = txtComment.getText().toString();
                String rating= String.valueOf(ratingBar.getRating());
               /* Toast.makeText(getApplicationContext(),
                        "MyID "+userId+
                                "  Rated User ID "+ratedUserId+
                                " Rated User Name  "+ratedUserName+
                                "  Rating= "+String.valueOf(ratingBar.getRating())+ comment,
                        Toast.LENGTH_SHORT).show();*/
                if(!rating.isEmpty() && !comment.isEmpty())
                {
                    postRating(userId,ratedUserId,rating,comment);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please Rate and comment both. Thank you...!", Toast.LENGTH_LONG)
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
     * function to verify login details in mysql db
     * */
    private void postRating(final String userId, final String ratedUserId, final String rating, final String comment){
        class PostRating extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityProfessionalDetail.this,"Posting...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                //getUser(s);
                //Toast.makeText(ActivityProfessionalDetail.this,s,Toast.LENGTH_LONG).show();
                Toast.makeText(ActivityProfessionalDetail.this,"Thank you for rating",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",userId);
                hashMap.put("rated_user_id",ratedUserId);
                hashMap.put("stars",rating);
                hashMap.put("comment",comment);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_POST_RATING,hashMap);

                return s;
            }
        }

        PostRating cul = new PostRating();
        cul.execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentProfessionalDetail(), "Detail");
        adapter.addFrag(new FragmentProfessionalRating(), "Rating");
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