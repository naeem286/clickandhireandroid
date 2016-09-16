package com.example.naeemasghar.clickandhire;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.naeemasghar.clickandhire.FragmentDashboard.ClickListener;
import com.example.naeemasghar.clickandhire.FragmentDashboard.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/11/2016.
 */
public class FragmentBrowseWork extends Fragment {

    private RecyclerView recyclerView;
    private AdapterCategory adapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_work,container,false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        initCollapsingToolbar(v);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
       // rv=(RecyclerView) layout.findViewById(R.id.rv);
        categoryList = new ArrayList<>();
        adapter = new AdapterCategory(getActivity(), categoryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) v.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = categoryList.get(position);
                Toast.makeText(getActivity(), category.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(getActivity(), ActivityWorkList.class);
                intent.putExtra("catName",category.getTitle());
                intent.putExtra("catId",category.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return v;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View v) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) v.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{

                R.drawable.icon_education,
                R.drawable.icon_software,
                R.drawable.icon_taxi,
                R.drawable.icon_even,
                R.drawable.icon_computer,
                R.drawable.icon_health,
                R.drawable.icon_beauty,
                R.drawable.icon_mover,
                R.drawable.icon_maid,
                R.drawable.icon_automobile,

        };

        Category a = new Category("Education","1", 100, covers[0]);
        categoryList.add(a);

        a = new Category("Software","2", 200, covers[1]);
        categoryList.add(a);

        a = new Category("Drivers & Taxi", "3",300, covers[2]);
        categoryList.add(a);

        a = new Category("Event Services","4", 500, covers[3]);
        categoryList.add(a);

        a = new Category("Electronics ","5", 4, covers[4]);
        categoryList.add(a);


        a = new Category("Health & Fitness","6", 6, covers[5]);
        categoryList.add(a);

        a = new Category("Beauty care","7", 7, covers[6]);
        categoryList.add(a);

        a = new Category("Movers & Packers","8",8, covers[7]);
        categoryList.add(a);

        a = new Category("Domestic Help","9", 9, covers[8]);
        categoryList.add(a);


        a = new Category("Services","10", 10, covers[9]);
        categoryList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
