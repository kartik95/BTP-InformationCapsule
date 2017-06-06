package btp.psychosocialeducationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Favorites extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView label;
    private TextView label1;
    private DBSingleton dbSingleton;
//    Long start,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbSingleton = DBSingleton.getInstance();
//        start = Calendar.getInstance().getTimeInMillis();
//        Log.d("LOG : ", "In Favorites Activity.");

        label = (TextView) findViewById(R.id.label);
        label1 = (TextView) findViewById(R.id.lable1);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);

        if (keysSet != null && !keysSet.isEmpty()) {

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new Favorites.GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            SavedRecyclerViewAdapter adapter = new SavedRecyclerViewAdapter(this, getNewsFeeds());
            recyclerView.setAdapter(adapter);
        }
        else {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setVisibility(View.INVISIBLE);
            label.setVisibility(View.VISIBLE);
            label1.setVisibility(View.VISIBLE);
        }
    }


    public void onResume(){
        super.onResume();

        String logString = "In Favorites Activity";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Current Position : ", "In Favorites Activity");

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

        String logString = "Time in FavActivity : " + elapsed/1000 + " seconds.";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Time in FavActivity : ", Long.toString(elapsed));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                end = Calendar.getInstance().getTimeInMillis();
//                Log.d("Time in Fav Activity : ", Long.toString(end-start));
//                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private HashMap<String, NewsFeed> getNewsFeeds() {

        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
        List<String> mapKeys = new ArrayList<>(keysSet);

        HashMap<String, NewsFeed> savedNewsFeeds = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new Favorites.UriDeserializer()).create();
        for (int i=0; i<mapKeys.size(); i++){
            String key = mapKeys.get(i);
            String json = prefs.getString(key, "");
            NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
            savedNewsFeeds.put(key, newsFeed);
        }
        return savedNewsFeeds;
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
