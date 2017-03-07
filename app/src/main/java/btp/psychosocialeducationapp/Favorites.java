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

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Favorites extends AppCompatActivity {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView label;
    private TextView label1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        label = (TextView) findViewById(R.id.label);
        label1 = (TextView) findViewById(R.id.lable1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);

        if (keysSet != null) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
//                Intent parentIntent = NavUtils.getParentActivityIntent(this);
//                if(parentIntent == null) {
//                    finish();
//                    return true;
//                } else {
////                    parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(parentIntent);
//                    finish();
//                    return true;
//                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private HashMap<String, NewsFeed> getNewsFeeds() {

        SharedPreferences prefs = this.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
//        Log.d("Prefs : ", prefs.toString());
        List<String> mapKeys = new ArrayList<>(keysSet);
        Log.d("Map Keys : ", mapKeys.toString());

        HashMap<String, NewsFeed> savedNewsFeeds = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new Favorites.UriDeserializer()).create();
        for (int i=0; i<mapKeys.size(); i++){
            String key = mapKeys.get(i);
            Log.d("Saved Key : ", key);
            String json = prefs.getString(key, "");
            Log.d("Saved Json String : ", json);
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
