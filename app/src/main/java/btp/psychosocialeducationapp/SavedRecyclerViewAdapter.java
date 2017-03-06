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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedViewHolder> {

    private Context context;
    private HashMap<String, NewsFeed> savedNewsFeeds;

    public SavedRecyclerViewAdapter(Context context, HashMap<String, NewsFeed> savedNewsFeeds) {
        this.context = context;
//        this.arrayList = arrayList;
//        this.titles = titles;
//        this.dates = dates;
//        this.imageUris = imageUris;
        this.savedNewsFeeds = savedNewsFeeds;
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
        NewsFeed savedNewsFeed;
        for (String key : savedNewsFeeds.keySet()) {
            savedNewsFeed = savedNewsFeeds.get(key);

//        final NewsFeed savedNewsFeed = savedNewsFeeds.get(Integer.toString(position));
            Log.d("Saved News Feed : ", savedNewsFeeds.toString());
            mainHolder.cardView.setTag(position);
            mainHolder.title.setText(savedNewsFeed.getTitle());
            mainHolder.date.setText(savedNewsFeed.getDate());
            mainHolder.image.setImageURI(savedNewsFeed.getImageUri());
        }
//        mainHolder.cardView.setTag(position);
//        mainHolder.title.setText("TITLE #1");
//        mainHolder.date.setText("DATE #1");
//        mainHolder.image.setImageURI("IMAGE #1");
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

        mainHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedNewsFeeds.remove(Integer.toString(position));
            }
        });
    }

}
