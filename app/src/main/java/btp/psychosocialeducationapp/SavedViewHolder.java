package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 5/3/17.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public abstract class SavedViewHolder extends RecyclerView.ViewHolder{


    public TextView title;
    public ImageView image;
    public TextView date;
    public CardView cardView;
    public Button remove;

    public SavedViewHolder(View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.title);
        this.image = (ImageView) view.findViewById(R.id.thumbnail);
        this.date = (TextView) view.findViewById(R.id.date);
        this.cardView = (CardView) view.findViewById(R.id.cardView);
        this.remove = (Button)  view.findViewById(R.id.remove);
    }


}
