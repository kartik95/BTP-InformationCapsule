package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 7/2/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
//    private ArrayList<String> arrayList;
    private ArrayList<String> titles;
    private ArrayList<String> dates;
    private ArrayList<Uri> imageUris;
    private Context context;
//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(MainActivity.class, Individual.class);
//        }
//    };

    public RecyclerViewAdapter(Context context,
                               ArrayList<String> titles, ArrayList<String> dates, ArrayList<Uri> imageUris) {
        this.context = context;
//        this.arrayList = arrayList;
        this.titles = titles;
        this.dates = dates;
        this.imageUris = imageUris;
    }


    @Override
    public int getItemCount() {
//        return (null != arrayList ? arrayList.size() : 0);

        return (null != titles ? titles.size() : 0);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {


        final ViewHolder mainHolder = holder;
        //Setting text over textview
//        mainHolder.title.setText(arrayList.get(position));
        mainHolder.title.setText(titles.get(position));
        mainHolder.date.setText(dates.get(position));
        mainHolder.image.setImageURI(imageUris.get(position));
        mainHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here goes your desired onClick behaviour. Like:
//                Toast.makeText(view.getContext(), "You have clicked " + view.getId(), Toast.LENGTH_SHORT).show(); //you can add data to the tag of your cardview in onBind... and retrieve it here with with.getTag().toString()..
//                //You can change the fragment, something like this, not tested, please correct for your desired output:
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Individual individual = new Individual();
//                //Create a bundle to pass data, add data, set the bundle to your fragment and:
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.bl, individual).addToBackStack(null).commit();
                Intent intent = new Intent(view.getContext(), Individual.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row, viewGroup, false);
        ViewHolder mainHolder = new ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }

}
