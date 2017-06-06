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
import android.widget.Toast;

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


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<NewsFeed> newsFeeds;
    private HashMap<String, NewsFeed> savedNewsFeeds;
    private Context context;
    private int flag=0;
    private DBSingleton dbSingleton;

    public RecyclerViewAdapter(Context context, ArrayList<NewsFeed> newsFeeds, HashMap<String, NewsFeed> savedNewsFeeds) {
        this.context = context;
        this.newsFeeds = newsFeeds;
        this.savedNewsFeeds = savedNewsFeeds;
        this.dbSingleton = DBSingleton.getInstance();
    }


    @Override
    public int getItemCount() {

        return (null != newsFeeds ? newsFeeds.size() : 0);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 final int position) {


        final ViewHolder mainHolder = holder;

        final NewsFeed newsFeed = newsFeeds.get(position);
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
        mainHolder.image.setImageURI(Uri.parse(newsFeed.getImageUri()));

        mainHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                intent.putExtra("title", newsFeeds.get(position).getTitle());
                intent.putExtra("date", newsFeeds.get(position).getDate());
                intent.putExtra("desc", newsFeeds.get(position).getDesc());
                intent.putExtra("image", newsFeeds.get(position).getImageUri());
                view.getContext().startActivity(intent);
            }
        });
        mainHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Individual.class);
                intent.putExtra("title", newsFeeds.get(position).getTitle());
                intent.putExtra("date", newsFeeds.get(position).getDate());
                intent.putExtra("desc", newsFeeds.get(position).getDesc());
                intent.putExtra("image", newsFeeds.get(position).getImageUri());
                view.getContext().startActivity(intent);
            }
        });

        mainHolder.save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NewsFeed newsFeed = newsFeeds.get(position);
                    mainHolder.save.setTextColor(Color.WHITE);
                    mainHolder.save.setBackgroundColor(Color.BLUE);
                    markAsFavourite(newsFeed, position);
                }
                else {
                    NewsFeed newsFeed = newsFeeds.get(position);
                    mainHolder.save.setTextColor(Color.BLACK);
                    mainHolder.save.setBackgroundColor(Color.WHITE);
                    removeFromFavourites(newsFeed, position);
                }
            }
        });
    }


    private void markAsFavourite(NewsFeed newsFeed, Integer position){

        String logString = newsFeed.getTitle() + "(" + position + ") Article was saved.";
        if(dbSingleton.insertLog(new LogData(context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(context, "Log entered.", Toast.LENGTH_SHORT).show();
        }

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

        savedNewsFeeds.put(Integer.toString(position), newsFeed);
        resetSavedNewsFeeds(savedNewsFeeds);
    }

    private void removeFromFavourites(NewsFeed newsFeed, int position){

        String logString = newsFeed.getTitle() + "(" + position + ") Article was unsaved.";
        if(dbSingleton.insertLog(new LogData(context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(context, "Log entered.", Toast.LENGTH_SHORT).show();
        }

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

        savedNewsFeeds.remove(Integer.toString(position));
        resetSavedNewsFeeds(savedNewsFeeds);
    }


    private void resetSavedNewsFeeds(HashMap<String, NewsFeed> savedNewsFeeds) {

        SharedPreferences.Editor editor = context.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE).edit();
        editor.clear();

        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create();
        List<String> mapKeys = new ArrayList<>(savedNewsFeeds.keySet());
        Set<String> keysSet = new HashSet<>(mapKeys);
        editor.putStringSet("mapKeys", keysSet);
        for(int i=0; i<savedNewsFeeds.size(); i++){
            String value = gson.toJson(savedNewsFeeds.get(mapKeys.get(i)));
            editor.putString(mapKeys.get(i), value);
        }
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
