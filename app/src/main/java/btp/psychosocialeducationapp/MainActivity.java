package btp.psychosocialeducationapp;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.cast.CastRemoteDisplay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import btp.psychosocialeducationapp.API.APIClient;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private DBSingleton dbSingleton;
    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbSingleton = DBSingleton.getInstance();

        getSharedPreferences("RegisteredStatus", Context.MODE_PRIVATE).edit().putString("regStatus", "R").apply();
//        active = true;
//        SharedPreferences.Editor editor = getSharedPreferences("Status", Context.MODE_PRIVATE).edit();
//        editor.putBoolean("status", active);
//        editor.commit();


//        start = Calendar.getInstance().getTimeInMillis();
//        start = System.currentTimeMillis();
////        timer = Timer.getInstance();
////        timer.startTime();
//        Log.d("LOG : ", "In Main Activity.");

//        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
//        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
//        if (keysSet != null) {
//            savedNewsFeeds = new HashMap<>();
//            List<String> mapKeys = new ArrayList<>(keysSet);
//            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new MainActivity.UriDeserializer()).create();
//            for (int i=0; i<mapKeys.size(); i++){
//                String key = mapKeys.get(i);
//                String json = prefs.getString(key, "");
//                NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
//                savedNewsFeeds.put(key, newsFeed);
//            }
//        }
//
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MainActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        ArrayList<NewsFeed> newsFeeds = getNewsFeeds(getTitles(), getDates(), getImageURIs(), getDescriptions());
//        if (savedNewsFeeds == null) {savedNewsFeeds = new HashMap<>();}
//        adapter = new RecyclerViewAdapter(this, newsFeeds, savedNewsFeeds);
//        recyclerView.setAdapter(adapter);

//        fab = (FloatingActionButton) findViewById(R.id.fab1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Favorites.class);
////                end = Calendar.getInstance().getTimeInMillis();
//////                timer.stopTime();
////                Log.d("Time in MainActivity : ", Long.toString(end-start));
//                startActivity(intent);
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void onResume(){
        super.onResume();
        active = true;
        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
        if (keysSet != null) {
            savedNewsFeeds = new HashMap<>();
            List<String> mapKeys = new ArrayList<>(keysSet);
            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new MainActivity.UriDeserializer()).create();
            for (int i=0; i<mapKeys.size(); i++){
                String key = mapKeys.get(i);
                String json = prefs.getString(key, "");
                NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
                savedNewsFeeds.put(key, newsFeed);
            }
        }

        ArrayList<NewsFeed> newsFeeds = getNewsFeeds(getTitles(), getDates(), getImageURIs(), getDescriptions());
//        ArrayList<NewsFeed> newsFeeds = getNewsFeeds();
        if (savedNewsFeeds == null) {savedNewsFeeds = new HashMap<>();}
        adapter = new RecyclerViewAdapter(this, newsFeeds, savedNewsFeeds);
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Favorites.class);
                startActivity(intent);
            }
        });

        fab1 = (FloatingActionButton) findViewById(R.id.fab);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APICalls apiCalls = new APICalls();
                apiCalls.sendApplicationLogs();
            }
        });

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APICalls apiCalls = new APICalls();
                apiCalls.sendUserInfo();
            }
        });

        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbSingleton.createCSVOfLogs(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "CSV created.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "CSV couldn't be created.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String logString = "In Main Activity";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Current Position : ", "In Main Activity");

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                    receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

        SharedPreferences.Editor editor = getSharedPreferences("Timer", Context.MODE_PRIVATE).edit();
        editor.putLong("time", System.currentTimeMillis());
        editor.commit();
    }

    public void onPause(){
        super.onPause();
        long end = System.currentTimeMillis();
        SharedPreferences prefs = getSharedPreferences("Timer", Context.MODE_PRIVATE);
        long start = prefs.getLong("time",0);
        long elapsed = end - start;

        String logString = "Time in Main Activity : " + elapsed/1000 + " seconds";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Time in MainActivity : ", Long.toString(elapsed));

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {

//            getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().remove("name").commit();
            getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().putString("alreadySignedIn", "-1").apply();

            active = false;
            getSharedPreferences("Status", Context.MODE_PRIVATE).edit().putBoolean("status", active).commit();

            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
//            mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
//                @Override
//                public void onResult(Status status) {
//                    mGoogleApiClient.disconnect();
//                }
//            });

            String logString = "Signed out successfully.";
            if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                    getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
                Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
            }

            LogData receivedLog = dbSingleton.getLastLog();
            Log.d("Received Last Log : ", "{" + receivedLog.getUserId() + ", " +
                    receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.edit_profile) {

            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){

        super.onBackPressed();
        active = false;
        SharedPreferences.Editor editor = getSharedPreferences("Status", Context.MODE_PRIVATE).edit();
        editor.putBoolean("status", active);
        editor.commit();
        finish();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    private ArrayList getTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.clear();
//        titles.add("What is Schizophrenia?");
//        titles.add("Symptoms of Schizophrenia");
//        titles.add("Causes of Schizophrenia");
//        titles.add("Therapy of Schizophrenia");
//        titles.add("Suggestions regarding Schizophrenia");
//        titles.add("Incidents of Schizophrenia");
        for(int i=0; i<6; i++){
            try {
                InputStream is = getResources().getAssets().open("Title" + Integer.toString(i) + ".txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String title = new String(buffer);
                titles.add(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return titles;
    }


    private ArrayList getDescriptions() {
        ArrayList<String> descs = new ArrayList<>();
        descs.clear();
//        descs.add("Schizophrenia is a mental disorder characterized by abnormal social behavior and failure to understand what is real.");
//        descs.add("Common symptoms include false beliefs, unclear or confused thinking, hearing voices that others do not, reduced social engagement and emotional expression, and a lack of motivation.");
//        descs.add("The causes of schizophrenia include environmental and genetic factors. Possible environmental factors include being raised in a city, cannabis use, certain infections, parental age, and poor nutrition during pregnancy.");
//        descs.add("Schizophrenia is a mental disorder characterized by abnormal social behavior and failure to understand what is real.");
//        descs.add("Common symptoms include false beliefs, unclear or confused thinking, hearing voices that others do not, reduced social engagement and emotional expression, and a lack of motivation.");
//        descs.add("The causes of schizophrenia include environmental and genetic factors. Possible environmental factors include being raised in a city, cannabis use, certain infections, parental age, and poor nutrition during pregnancy.")
        try{
            for(int i=0; i<6; i++) {
                InputStream is = getResources().getAssets().open("Desc" + Integer.toString(i) + ".txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String text = new String(buffer);
                descs.add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return descs;
    }


    private ArrayList getDates() {
        ArrayList<String> dates = new ArrayList<>();
        dates.add("January 28th, 2017");
        dates.add("February 2nd, 2017");
        dates.add("February14th, 2017");
        dates.add("January 28th, 2017");
        dates.add("February 2nd, 2017");
        dates.add("February14th, 2017");
        return dates;
    }


    private ArrayList<String> getImageURIs() {
        ArrayList<String> imageURIs = new ArrayList<>();
        for (int i =1; i<=6; i++){
            Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp" + i);
            imageURIs.add(uri.toString());
        }
        return imageURIs;
    }


//    private NewsFeed getWhatNewsFeed() {
//        String title = null;
//        try{
//            InputStream is = getResources().getAssets().open("what_title.txt");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            title = new String(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String desc = null;
//        try{
//            InputStream is = getResources().getAssets().open("what.txt");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            desc = new String(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp1");
//        String date = "January 10, 2017";
//
//        return new NewsFeed(title, date, uri.toString(), desc);
//    }
//
//
//    private NewsFeed getCausesNewsFeed() {
//        String title = null;
//        try{
//            InputStream is = getResources().getAssets().open("causes_title.txt");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            title = new String(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String desc = "";
//        try{
//            for(int i=0; i<8; i++) {
//                InputStream is = getResources().getAssets().open("causes" + Integer.toString(i) + ".txt");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                desc += new String(buffer) + " & ";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp2");
//        String date = "March 30, 2017";
//
//        return new NewsFeed(title, date, uri.toString(), desc);
//    }
//
//
//    private NewsFeed getSymptomsNewsFeed() {
//        String title = "";
//        try{
//            InputStream is = getResources().getAssets().open("symptoms_title.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            String line = reader.readLine();
//            while (line != null) {
//                title += line + " & ";
//                line = reader.readLine();
//            }
////            int size = is.available();
////            byte[] buffer = new byte[size];
////            is.read(buffer);
////            is.close();
//
////            title = new String(buffer);
//            reader.close();
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String desc = "";
//        try{
//            for(int i=0; i<5; i++) {
//                InputStream is = getResources().getAssets().open("symptoms" + Integer.toString(i) + ".txt");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                desc += new String(buffer) + " & ";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp3");
//        String date = "February 12, 2017";
//
//        return new NewsFeed(title, date, uri.toString(), desc);
//    }
//
//
//    private NewsFeed getDiagnosisNewsFeed() {
//        String title = "";
//        try{
//            InputStream is = getResources().getAssets().open("diagnosis_title.txt");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            title = new String(buffer);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String desc = "";
//        try{
//            for(int i=0; i<3; i++) {
//                InputStream is = getResources().getAssets().open("diagnosis" + Integer.toString(i) + ".txt");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                desc += new String(buffer) + " & ";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/btp4");
//        String date = "December 18, 2016";
//
//        return new NewsFeed(title, date, uri.toString(), desc);
//    }
//
//
//    private ArrayList<NewsFeed> getNewsFeeds() {
//        ArrayList<NewsFeed> newsFeeds = new ArrayList<>();
//        newsFeeds.add(getWhatNewsFeed());
//        newsFeeds.add(getSymptomsNewsFeed());
//        newsFeeds.add(getCausesNewsFeed());
//        newsFeeds.add(getDiagnosisNewsFeed());
//        return newsFeeds;
//    }


    private ArrayList<NewsFeed> getNewsFeeds(ArrayList<String> titles, ArrayList<String> dates, ArrayList<String> imageUris,
                                             ArrayList<String> descs) {
        ArrayList<NewsFeed> newsFeeds = new ArrayList<>();
        for(int i=0; i<titles.size(); i++) {
            NewsFeed newsFeed = new NewsFeed(titles.get(i), dates.get(i), imageUris.get(i), descs.get(i));
            newsFeeds.add(newsFeed);
        }
        return newsFeeds;
    }

    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }

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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}