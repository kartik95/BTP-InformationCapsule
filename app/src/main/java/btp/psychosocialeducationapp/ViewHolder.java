package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 7/2/17.
 */

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ViewHolder extends RecyclerView.ViewHolder{


    public TextView title;
    public ImageView image;
    public TextView date;
    public CardView cardView;

    public ViewHolder(View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.title);
        this.image = (ImageView) view.findViewById(R.id.thumbnail);
        this.date = (TextView) view.findViewById(R.id.date);
        this.cardView = (CardView) view.findViewById(R.id.cardView);
    }


}
