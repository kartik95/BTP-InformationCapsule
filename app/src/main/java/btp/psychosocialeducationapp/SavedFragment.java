package btp.psychosocialeducationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavedFragment extends Fragment {

    private View view;
    private static RecyclerView recyclerView;
    boolean fragmentAlreadyLoaded = false;

    public SavedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_saved, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
//        Log.d("Map Keys : ", keysSet.toString());
//        if (keysSet != null) {
////            setRecyclerView();
//
//
//
//            adapter.notifyDataSetChanged();
//
//
//        }
        if (savedInstanceState == null && !fragmentAlreadyLoaded) {
            fragmentAlreadyLoaded = true;
            if (keysSet != null) {

                recyclerView = (RecyclerView) view
                        .findViewById(R.id.recyclerView1);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new SavedFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                SavedRecyclerViewAdapter adapter = new SavedRecyclerViewAdapter(getActivity(), getNewsFeeds());
                recyclerView.setAdapter(adapter);
            }
            // Code placed here will be executed once

        }
        if (keysSet != null) {
            SavedRecyclerViewAdapter adapter = new SavedRecyclerViewAdapter(getActivity(), getNewsFeeds());
//            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        //Code placed here will be executed even when the fragment comes from backstack
    }


    private HashMap<String, NewsFeed> getNewsFeeds() {

        SharedPreferences prefs = getContext().getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
//        Log.d("Prefs : ", prefs.toString());
        List<String> mapKeys = new ArrayList<>(keysSet);
        Log.d("Map Keys : ", mapKeys.toString());

        HashMap<String, NewsFeed> savedNewsFeeds = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create();
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
            return Uri.parse(src.toString());
        }
    }
    //Setting recycler view
//    private void setRecyclerView() {
//
//        recyclerView = (RecyclerView) view
//                .findViewById(R.id.recyclerView1);
//        recyclerView.setHasFixedSize(true);
////        recyclerView
////                .setLayoutManager(new LinearLayoutManager(getActivity()));//Linear Items
//
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new SavedFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
////        ArrayList<String> arrayList = new ArrayList<>();
////        for (int i = 0; i < 20; i++) {
////            arrayList.add(title+" Items " + i);//Adding items to recycler view
////        }
//        SavedRecyclerViewAdapter adapter = new SavedRecyclerViewAdapter(getActivity(), getNewsFeeds());
//        recyclerView.setAdapter(adapter);// set adapter on recyclerview
//
//    }


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
