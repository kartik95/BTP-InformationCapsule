package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 5/3/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedViewHolder> {

    private Context context;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private ArrayList<NewsFeed> feeds;
    private ArrayList<String> feedPositions;

    public SavedRecyclerViewAdapter(Context context, HashMap<String, NewsFeed> savedNewsFeeds) {
        this.context = context;
//        this.arrayList = arrayList;
//        this.titles = titles;
//        this.dates = dates;
//        this.imageUris = imageUris;
        feeds = new ArrayList<>();
        feedPositions = new ArrayList<>();
        this.savedNewsFeeds = savedNewsFeeds;
        for (String key : savedNewsFeeds.keySet()) {
            feeds.add(savedNewsFeeds.get(key));
            feedPositions.add(key);
        }
    }

    public int getItemCount() {

        return (null != savedNewsFeeds ? savedNewsFeeds.size() : 0);
    }


    public SavedViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        View mainGroup = mInflater.inflate(
                R.layout.saved_item_row, viewGroup, false);

        SavedViewHolder mainHolder = new SavedViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        return mainHolder;

    }

    @Override
    public void onBindViewHolder(SavedViewHolder holder, final int position) {

        final SavedViewHolder mainHolder = holder;
//        for (String key : savedNewsFeeds.keySet()) {
//            NewsFeed savedNewsFeed = savedNewsFeeds.get(key);
//
////        final NewsFeed savedNewsFeed = savedNewsFeeds.get(Integer.toString(position));
////            Log.d("Saved News Feed : ", savedNewsFeeds.toString());
//            Log.d("Title : ", savedNewsFeed.getTitle());
//            Log.d("Date : ", savedNewsFeed.getDate());
//            mainHolder.cardView.setTag(position);
//            mainHolder.title.setText(savedNewsFeed.getTitle());
//            mainHolder.date.setText(savedNewsFeed.getDate());
//            mainHolder.image.setImageURI(savedNewsFeed.getImageUri());
//        }
//        mainHolder.cardView.setTag(position);
//        mainHolder.title.setText("TITLE #1");
//        mainHolder.date.setText("DATE #1");
//        mainHolder.image.setImageURI("IMAGE #1");
        final NewsFeed savedNewsFeed = feeds.get(position);
        mainHolder.cardView.setTag(position);
        mainHolder.title.setText(savedNewsFeed.getTitle());
        mainHolder.date.setText(savedNewsFeed.getDate());
        mainHolder.image.setImageURI(savedNewsFeed.getImageUri());
        mainHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                intent.putExtra("title", feeds.get(position).getTitle());
                intent.putExtra("date", feeds.get(position).getDate());
                intent.putExtra("desc", feeds.get(position).getDesc());
                intent.putExtra("image", feeds.get(position).getImageUri());
                view.getContext().startActivity(intent);
            }
        });
        mainHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                intent.putExtra("title", feeds.get(position).getTitle());
                intent.putExtra("date", feeds.get(position).getDate());
                intent.putExtra("desc", feeds.get(position).getDesc());
                intent.putExtra("image", feeds.get(position).getImageUri());
                view.getContext().startActivity(intent);
            }
        });

        mainHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                savedNewsFeeds.remove(Integer.toString(position));
                String removedFeedPosition = feedPositions.get(position);
//                NewsFeed removedNewsFeed = feeds.get(position);
                feeds.remove(position);
                feedPositions.remove(position);
                savedNewsFeeds.remove(removedFeedPosition);

                SharedPreferences.Editor editor = context.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE).edit();
//                editor.remove(removedFeedPosition);
                editor.clear();
//
                Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new SavedRecyclerViewAdapter.UriSerializer()).create();
                Set<String> keysSet = new HashSet<>(feedPositions);
                editor.putStringSet("mapKeys", keysSet);
                for (int i=0; i<feeds.size(); i++) {
                    String value = gson.toJson(feeds.get(i));
                    editor.putString(feedPositions.get(i), value);
                }
                editor.apply();
                editor.commit();

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, savedNewsFeeds.size());
            }
        });
    }

    public class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}
