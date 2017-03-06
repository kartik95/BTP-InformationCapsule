package btp.psychosocialeducationapp;

//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//public class EditActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//}

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class EditActivity extends AppCompatActivity {

    private EditText age;
    private EditText gender;
    private EditText alternatePhone;
    private EditText phone;
    private TextView userName;
    private TextView emailID;
    private ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set EditProfile fields with already present values of Profile Inf

        //Set Profile Information
        getProfileInformation();

        gender = (EditText) findViewById(R.id.et); gender.setSelection(gender.getText().length());
        age = (EditText) findViewById(R.id.et1); age.setSelection(age.getText().length());
        phone = (EditText) findViewById(R.id.et2); phone.setSelection(phone.getText().length());
        alternatePhone = (EditText) findViewById(R.id.et3); alternatePhone.setSelection(alternatePhone.getText().length());
        SharedPreferences prefs = getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE);
        gender.setText(prefs.getString("gender",""));
        age.setText(prefs.getString("age",""));
        phone.setText(prefs.getString("phone",""));
        alternatePhone.setText(prefs.getString("alternatePhone",""));
    }

    private void getProfileInformation(){

        userName = (TextView) findViewById(R.id.tv1);
        emailID = (TextView) findViewById(R.id.tv2);
        profileImg = (ImageView) findViewById(R.id.label);
        SharedPreferences prefs = getSharedPreferences("My_Prefs", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String email = prefs.getString("email", "");
        String url = prefs.getString("url", "");
        userName.setText(name);
        emailID.setText(email);
        if(url != null){
            new LoadProfileImage(profileImg).execute(url);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_done) {
            //Send final EditProfile values to Profile Info
            EditText gender1 = (EditText) findViewById(R.id.et);
            EditText age1 = (EditText) findViewById(R.id.et1);
            EditText phone1 = (EditText) findViewById(R.id.et2);
            EditText alternatePhone1 = (EditText) findViewById(R.id.et3);
            SharedPreferences.Editor editor = getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE).edit();
            editor.putString("gender", gender1.getText().toString());
            editor.putString("age", age1.getText().toString());
            editor.putString("phone", phone1.getText().toString());
            editor.putString("alternatePhone", alternatePhone1.getText().toString());
            editor.commit();

            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {


                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(EditActivity.this,resized,250,200,200, false, false, false, false));

            }
        }
    }

}
