package com.example.naeemasghar.clickandhire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubCategoryList extends AppCompatActivity {

    private List<SubCategory> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterSubCategory nAdapter;
    private SQLiteHandler db;
    private ArrayList<String> subCategoryLis;
    private TextView txtCatHeading;
    private String catId;
    private String catName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_sub_category_list);;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Categories");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtCatHeading=(TextView)findViewById(R.id.cat_heading);
        catName= getIntent().getStringExtra("catName");
        catId= getIntent().getStringExtra("catId");
        txtCatHeading.setText("Sub Categories of  "+catName);
        // SqLite database handler
        db = new SQLiteHandler(this);
        subCategoryLis= new ArrayList<String>();
        subCategoryLis=db.fetchSubCategoryNames(catId);
        nAdapter = new AdapterSubCategory(subCategoryLis);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerNewsDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String subCatName = subCategoryLis.get(position);
                //Toast.makeText(getApplicationContext(), subCatName  + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(ActivitySubCategoryList.this, ActivityPostWork.class);
                intent.putExtra("catId", catId);
                intent.putExtra("subCatId", Integer.toString(position+1));
                intent.putExtra("catName", subCatName);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    private void prepareMovieData() {
        SubCategory movie = new SubCategory("Mad Max: Fury Road", "Action & Adventure", "12-10-2015");
        newsList.add(movie);

        movie = new SubCategory("Inside Out", "Animation, Kids & Family", "2015");
        newsList.add(movie);

        movie = new SubCategory("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        newsList.add(movie);

        movie = new SubCategory("Shaun the Sheep", "Animation", "2015");
        newsList.add(movie);

        movie = new SubCategory("The Martian", "Science Fiction & Fantasy", "2015");
        newsList.add(movie);

        movie = new SubCategory("Mission: Impossible Rogue Nation", "Action", "2015");
        newsList.add(movie);

        movie = new SubCategory("Up", "Animation", "2009");
        newsList.add(movie);

        movie = new SubCategory("Star Trek", "Science Fiction", "2009");
        newsList.add(movie);

        movie = new SubCategory("The LEGO Movie", "Animation", "2014");
        newsList.add(movie);

        movie = new SubCategory("Iron Man", "Action & Adventure", "2008");
        newsList.add(movie);

        movie = new SubCategory("Aliens", "Science Fiction", "1986");
        newsList.add(movie);

        movie = new SubCategory("Chicken Run", "Animation", "2000");
        newsList.add(movie);

        movie = new SubCategory("Back to the Future", "Science Fiction", "1985");
        newsList.add(movie);

        movie = new SubCategory("Raiders of the Lost Ark", "Action & Adventure", "1981");
        newsList.add(movie);

        movie = new SubCategory("Goldfinger", "Action & Adventure", "1965");
        newsList.add(movie);

        movie = new SubCategory("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        newsList.add(movie);

        nAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
