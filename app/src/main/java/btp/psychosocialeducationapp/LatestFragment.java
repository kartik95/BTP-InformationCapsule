package btp.psychosocialeducationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static btp.psychosocialeducationapp.R.id.recyclerView;
import static btp.psychosocialeducationapp.R.id.title;

public class LatestFragment extends Fragment {

    private View view;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private static RecyclerView recyclerView;

    public LatestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_latest, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
        if (keysSet != null) {
            savedNewsFeeds = new HashMap<>();
            List<String> mapKeys = new ArrayList<>(keysSet);
            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create();
            for (int i=0; i<mapKeys.size(); i++){
                String key = mapKeys.get(i);
                Log.d("Key : ", key);
                String json = prefs.getString(key, "");
                Log.d("Json : ", json);
                NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
                savedNewsFeeds.put(key, newsFeed);
            }
        }

        setRecyclerView();
        return view;

    }

    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.toString());
        }
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
        Log.d("Titles : ", titles.toString());
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
            Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/btp" + i);
            imageURIs.add(uri.toString());
        }
        return imageURIs;
    }


    private ArrayList<NewsFeed> getNewsFeeds(ArrayList<String> titles, ArrayList<String> dates, ArrayList<String> imageUris,
                                             ArrayList<String> descs) {
        ArrayList<NewsFeed> newsFeeds = new ArrayList<>();
        for(int i=0; i<titles.size(); i++) {
            NewsFeed newsFeed = new NewsFeed(titles.get(i), dates.get(i), imageUris.get(i), descs.get(i));
            newsFeeds.add(newsFeed);
        }
        return newsFeeds;
    }


    //Setting recycler view
    private void setRecyclerView() {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<NewsFeed> newsFeeds = getNewsFeeds(getTitles(), getDates(), getImageURIs(), getDescriptions());
        if (savedNewsFeeds == null) {savedNewsFeeds = new HashMap<>();}
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), newsFeeds, savedNewsFeeds);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview

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