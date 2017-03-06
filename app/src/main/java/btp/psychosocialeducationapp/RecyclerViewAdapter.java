package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 7/2/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.security.AccessController.getContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
//    private ArrayList<String> arrayList;
//    private ArrayList<String> titles;
//    private ArrayList<String> dates;
//    private ArrayList<Uri> imageUris;
//    private ArrayList<String> descs;
    private ArrayList<NewsFeed> newsFeeds;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private Context context;
    private int flag=0;
//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override

//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(MainActivity.class, Individual.class);
//        }
//    };

    public RecyclerViewAdapter(Context context, ArrayList<NewsFeed> newsFeeds, HashMap<String, NewsFeed> savedNewsFeeds) {
        this.context = context;
//        this.arrayList = arrayList;
//        this.titles = titles;
//        this.dates = dates;
//        this.imageUris = imageUris;
        this.newsFeeds = newsFeeds;
        this.savedNewsFeeds = savedNewsFeeds;
    }


    @Override
    public int getItemCount() {

        return (null != newsFeeds ? newsFeeds.size() : 0);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 final int position) {


        final ViewHolder mainHolder = holder;

        NewsFeed newsFeed = newsFeeds.get(position);
        for (String key : savedNewsFeeds.keySet()){
            if (position == Integer.parseInt(key)) {
                mainHolder.save.setChecked(true);
                mainHolder.save.setBackgroundColor(Color.BLUE);
                mainHolder.save.setTextColor(Color.WHITE);
            }
        }
        mainHolder.cardView.setTag(position);
        mainHolder.title.setText(newsFeed.getTitle());
        mainHolder.date.setText(newsFeed.getDate());
        mainHolder.image.setImageURI(newsFeed.getImageUri());

        mainHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                view.getContext().startActivity(intent);
            }
        });
        mainHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                view.getContext().startActivity(intent);
            }
        });

        mainHolder.save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NewsFeed newsFeed = newsFeeds.get(position);
//                    String title = newsFeed.getTitle();
//                    String date = newsFeed.getDate();
//                    Uri imageUri = newsFeed.getImageUri();
//                    String desc = newsFeed.getDesc();

                    mainHolder.save.setTextColor(Color.WHITE);
                    mainHolder.save.setBackgroundColor(Color.BLUE);
//                    markAsFavourite(title, date, imageUri, desc, position);
                    markAsFavourite(newsFeed, position);
                }
                else {
                    mainHolder.save.setTextColor(Color.BLACK);
                    mainHolder.save.setBackgroundColor(Color.WHITE);
                    removeFromFavourites(position);
                }
            }
        });
//        mainHolder.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(v.isSelected()) {
//                    String title = titles.get(position);
//                    String date = dates.get(position);
//                    Uri imageUri = imageUris.get(position);
//
//                    markAsFavourite(title, date, imageUri);
//                    mainHolder.save.setTextColor();
//                    mainHolder.save.back
//                } else {
//                    removeFromFavourites(position);
//                }
//            }
//        });
    }


//    private void markAsFavourite(String title, String date, Uri imageUri, String desc, int position){
//
//        savedNewsFeeds.put(Integer.toString(position), new NewsFeed(title, date, imageUri, desc));
//        Log.d("Saved", "Marked as favorite");
//        resetSavedNewsFeeds(savedNewsFeeds);
//    }

    private void markAsFavourite(NewsFeed newsFeed, Integer position){

        savedNewsFeeds.put(Integer.toString(position), newsFeed);
//        Log.d("Saved", "Marked as favorite");
        resetSavedNewsFeeds(savedNewsFeeds);
//        notifyDataSetChanged();
//        Fragment frag = ((Activity) context).getFragmentManager().findFragmentById(R.id.frag);
    }

    private void removeFromFavourites(int position){

        savedNewsFeeds.remove(Integer.toString(position));
//        Log.d("Unsaved", "Removed as favorite");
        resetSavedNewsFeeds(savedNewsFeeds);
//        notifyDataSetChanged();
//        SharedPreferences.Editor editor = context.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE).edit();
//        editor.clear();
//        editor.commit();
//
//        Gson gson = new Gson();
//        String json = gson.toJson(savedNewsFeeds);
//
//        editor.putString("savedNewsFeeds", json);
//        editor.commit();
    }


    private void resetSavedNewsFeeds(HashMap<String, NewsFeed> savedNewsFeeds) {

        Log.d("SavedNewsFeeds : ", savedNewsFeeds.toString());
        SharedPreferences.Editor editor = context.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE).edit();
        editor.clear();
//        editor.commit();

        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create();
        List<String> mapKeys = new ArrayList<>(savedNewsFeeds.keySet());
        Set<String> keysSet = new HashSet<>(mapKeys);
        editor.putStringSet("mapKeys", keysSet);
        for(int i=0; i<savedNewsFeeds.size(); i++){
//            Log.d("Obtained Value : ", savedNewsFeeds.get(i).toString());
            String value = gson.toJson(savedNewsFeeds.get(mapKeys.get(i)));
            Log.d("Saved Value : ", value);
            editor.putString(mapKeys.get(i), value);
        }
//        String json = gson.toJson(savedNewsFeeds);

//        editor.putString("savedNewsFeeds", json);
        editor.apply();
        editor.commit();
    }


    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        View mainGroup = mInflater.inflate(
                R.layout.item_row, viewGroup, false);

        ViewHolder mainHolder = new ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        return mainHolder;

    }

    public class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}
