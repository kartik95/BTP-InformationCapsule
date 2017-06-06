package btp.psychosocialeducationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static btp.psychosocialeducationapp.R.id.fab;
import static btp.psychosocialeducationapp.R.id.parallax;

public class Individual extends AppCompatActivity {

    private FloatingActionButton fab;
    private DBSingleton dbSingleton;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbSingleton = DBSingleton.getInstance();
        activityTitle = getIntent().getStringExtra("title");

        TextView title = (TextView)  findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        ImageView image = (ImageView) findViewById(R.id.backdrop);

        this.setTitle(activityTitle);
        title.setText(getIntent().getStringExtra("title"));
        desc.setText(getIntent().getStringExtra("desc"));
//        image.setImageURI(Uri.parse(getIntent().getStringExtra("image")));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void onResume(){
        super.onResume();

        String logString = "In Individual (" + activityTitle + ") Activity";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Current Position : ", "In Individual Activity");

//        List<LogData> receivedLogs = dbSingleton.getAllLogs();
//        for (int i=0; i<receivedLogs.size(); i++) {
//            Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                    receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//        }
        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
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

        String logString = "Time in Individual (" + activityTitle + ") Activity : " + elapsed/1000 + " seconds.";
        if(dbSingleton.insertLog(new LogData(getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
            Toast.makeText(getApplicationContext(), "Log entered.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Time in IndActivity : ", Long.toString(elapsed));

        LogData receivedLog = dbSingleton.getLastLog();
        Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
//                Intent parentIntent = NavUtils.getParentActivityIntent(this);
//                if(parentIntent == null) {
//                    finish();
//                    return true;
//                } else {
//                    parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(parentIntent);
//                    finish();
//                    return true;
//                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


