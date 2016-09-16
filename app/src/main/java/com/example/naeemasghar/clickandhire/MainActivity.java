package com.example.naeemasghar.clickandhire;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    //Defining Variables
    private SQLiteHandler db;
    private SessionManager session;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RelativeLayout navHeader;
    private ImageView profileImage;
    private TextView txtName;
    private TextView txtEmail;
    public ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLoader = new ImageLoader(this);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Click and Hire");
        //getActionBar().setTitle("Click and Hire");
        checkConnection();
        // SqLite database handler
        db = new SQLiteHandler(this);

        HashMap<String, String> user = db.getUserDetails();
        String uid = user.get("id");
        String name = user.get("name");
        String phone = user.get("phone");
        String email = user.get("email");


        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //navHeader= (RelativeLayout)findViewById(R.id.nav_header);

        View headerview = navigationView.getHeaderView(0);
        navHeader= (RelativeLayout) headerview.findViewById(R.id.nav_header);
        txtName=(TextView)headerview.findViewById(R.id.username);
        txtEmail=(TextView)headerview.findViewById(R.id.email);
        profileImage=(ImageView) headerview.findViewById(R.id.profile_image);
        //profileImage.setImageBitmap(getThumbnail("profile.png"));
        //profileImage.setImageResource(R.drawable.profile);

        String urlForImage = AppConfig.URL_For_Image + uid+".jpg";
        imageLoader.DisplayImage(urlForImage, profileImage);

        txtName.setText(name);
        txtEmail.setText(email);


        //  to test
        FragmentDashboard fragment = new FragmentDashboard();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        /// end test
        // Login button Click Event

        navHeader.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Launch Login Activity
                //Toast.makeText(getApplicationContext(), "Profile is Selected", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(MainActivity.this,ActivityMyProfile.class);
                startActivity(intent);
            }
        });
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.nav_header:
                        Toast.makeText(getApplicationContext(), "Profile is Selected", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(MainActivity.this,ActivityMyProfile.class);
                        startActivity(intent);
                        return true;

                    // For rest of the options we just show a toast on click

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.dashboard:
                        Toast.makeText(getApplicationContext(), "Dashboard Selected", Toast.LENGTH_SHORT).show();

                        FragmentDashboard fragment = new FragmentDashboard();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click
                    case R.id.browse_professional:
                        Toast.makeText(getApplicationContext(), "Browse Professional Selected", Toast.LENGTH_SHORT).show();

                        FragmentProfessional fragmentProfessional = new FragmentProfessional();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame, fragmentProfessional);
                        fragmentTransaction1.commit();

                        return true;
                    case R.id.browse_work:

                        FragmentBrowseWork browseWork = new FragmentBrowseWork();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, browseWork);
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.post_work:
                        FragmentPostWrok fragmentPostWrok = new FragmentPostWrok();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame, fragmentPostWrok);
                        fragmentTransaction3.commit();
                        return true;
                    case R.id.my_work:
                        /*FragmentMyWork fragmentMyWork = new FragmentMyWork();
                        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.frame, fragmentMyWork);
                        fragmentTransaction4.commit();*/
                        Intent intent1= new Intent(MainActivity.this,ActivityMyWork.class);
                        startActivity(intent1);
                        return true;
                    case R.id.logout:
                        logoutUser();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
        startActivity(intent);
        finish();
        deleteProfile();
    }
    private void deleteProfile()
    {
        File dir = getFilesDir();
        File file = new File(dir, "profile.png");
        boolean deleted = file.delete();
        File file1 = new File("profile.png");
        file1.delete();
    }

    public Bitmap getThumbnail(String filename) {


        Bitmap thumbnail = null;


        // look in internal storage
        try {
            Context context=this;
            File filePath = context.getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filePath);
            thumbnail = BitmapFactory.decodeStream(fi);
        } catch (Exception ex) {
            Log.e("get Image storage", ex.getMessage());
        }
        return thumbnail;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
           Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            message = "Sorry! Not connected to internet";
            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }

        //Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}