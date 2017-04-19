package btp.psychosocialeducationapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import btp.psychosocialeducationapp.API.APIClient;

public class EditActivity extends AppCompatActivity {

    private EditText age;
    private EditText gender;
    private EditText alternatePhone;
    private EditText phone;
    private TextView userName;
    private TextView emailID;
    private ImageView profileImg;
    private DBSingleton dbSingleton;
    private APIClient mApiClient;
//    long start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mApiClient = new APIClient();
//
//        if(getSharedPreferences("LoginResponse", Context.MODE_PRIVATE).getString("response", null).equals("failure")) {
//
//        }
        dbSingleton = DBSingleton.getInstance();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        start = Calendar.getInstance().getTimeInMillis();
////        timer = Timer.getInstance();
////        timer.startTime();
//        Log.d("LOG : ", "In Edit Activity.");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set EditProfile fields with already present values of Profile Inf

        //Set Profile Information
        getProfileInformation();

        gender = (EditText) findViewById(R.id.et);
        age = (EditText) findViewById(R.id.et1);
        phone = (EditText) findViewById(R.id.et2);
        alternatePhone = (EditText) findViewById(R.id.et3);

        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);

        if (!getSharedPreferences("Status", Context.MODE_PRIVATE).getBoolean("status", false)) {
            gender.setText("अपना लिंग दर्ज करें");
            gender.setTextColor(Color.parseColor("#666666"));
            age.setText("अपनी आयु दर्ज करें");
            age.setTextColor(Color.parseColor("#666666"));
            phone.setText("अपना प्राथमिक संपर्क दर्ज करें");
            phone.setTextColor(Color.parseColor("#666666"));
            alternatePhone.setText("अपना वैकल्पिक संपर्क दर्ज करें");
            alternatePhone.setTextColor(Color.parseColor("#666666"));
        }
        else {
            SharedPreferences prefs = getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE);
            gender.setText(prefs.getString("gender",""));
            age.setText(prefs.getString("age",""));
            phone.setText(prefs.getString("phone",""));
            alternatePhone.setText(prefs.getString("alternatePhone",""));
        }

        gender.setSelection(gender.getText().length());
        age.setSelection(age.getText().length());
        phone.setSelection(phone.getText().length());
        alternatePhone.setSelection(alternatePhone.getText().length());
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

            SharedPreferences prefs = getSharedPreferences("Status", Context.MODE_PRIVATE);
            if (!prefs.getBoolean("status", false)) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onResume(){
        super.onResume();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String logString = "In Edit Activity";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Current Position : ", "In Edit Activity");

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

        SharedPreferences.Editor editor = getSharedPreferences("Timer", Context.MODE_PRIVATE).edit();
        editor.putLong("time", System.currentTimeMillis());
        editor.commit();
    }

    public void onPause(){
        super.onPause();
        long end = System.currentTimeMillis();

        SharedPreferences prefs = getSharedPreferences("Timer", Context.MODE_PRIVATE);
        long start = prefs.getLong("time",0);

        long elapsed = end - start;

        String logString = "Time in EditActivity : " + elapsed/1000 + " seconds.";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Time in EditActivity : ", Long.toString(elapsed));

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");
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
