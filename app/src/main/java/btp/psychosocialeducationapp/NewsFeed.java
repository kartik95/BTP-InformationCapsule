package btp.psychosocialeducationapp;

import android.net.Uri;

/**
 * Created by gkartik on 17/2/17.
 */

public class NewsFeed {

    private String title;
    private String date;
    private Uri imageUri;
    private String desc;

    public NewsFeed(String title, String date, Uri imageUri, String desc){
        this.title = title;
        this.date = date;
        this.imageUri = imageUri;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getDesc() { return desc;}

    public void setDesc(String desc) { this.desc = desc;}
}
