package btp.psychosocialeducationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import btp.psychosocialeducationapp.API.APIClient;
import btp.psychosocialeducationapp.API.ApplicationLogsPost;
import btp.psychosocialeducationapp.API.Response;
import btp.psychosocialeducationapp.API.User;
import btp.psychosocialeducationapp.API.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by gkartik on 19/4/17.
 */

public class APICalls {

    InfoCapsuleApp appInstance = InfoCapsuleApp.getInstance();

    public void sendApplicationLogs() {
        DBSingleton dbSingleton = DBSingleton.getInstance();
        List<ApplicationLogsPost> applicationLogs = new ArrayList<>();

        List<LogData> receivedLogs = dbSingleton.getAllLogs();
        for (int i=0; i<receivedLogs.size(); i++) {
            applicationLogs.add(new ApplicationLogsPost(new UserInfo(receivedLogs.get(i).getUserId(),
                    receivedLogs.get(i).getEmail()), new btp.psychosocialeducationapp.API.Log(receivedLogs.get(i).getTimeStamp(),
                    receivedLogs.get(i).getLog())));
        }

        new APIClient().getUserAPI().sendApplicationLogs(applicationLogs.subList(applicationLogs.size() - 4, applicationLogs.size()), new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                if (response.response.equals("success")) {
                    Toast.makeText(appInstance.getApplicationContext(), "Application Logs sent.", Toast.LENGTH_SHORT).show();
                    Log.d("SendDataPreviousDay : ", "Application Logs sent.");
                }
                else {
                    Log.d("SendDataPreviousDay : ", "Application Logs not sent.");
                    Toast.makeText(appInstance.getApplicationContext(), "Application not Logs sent.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(appInstance.getApplicationContext(),"Application Logs not sent.",Toast.LENGTH_SHORT).show();
                Log.d("SendDataPreviousDay : ", "Application logs not sent (Retro error).");
            }
        });
    }


    public void sendUserInfo() {

//        HashMap<String, NewsFeed> savedNewsFeeds = new HashMap<>();
        List<String> mapKeys = new ArrayList<>();
        List<NewsFeed> savedNewsFeeds = new ArrayList<>();
        SharedPreferences prefs = appInstance.getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE);
        Set<String> keysSet = prefs.getStringSet("mapKeys", null);
        if (keysSet != null) {
//            savedNewsFeeds = new HashMap<>();
            mapKeys = new ArrayList<>(keysSet);
            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new APICalls.UriDeserializer()).create();
            for (int i=0; i<mapKeys.size(); i++){
                String key = mapKeys.get(i);
                String json = prefs.getString(key, "");
                NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
//                savedNewsFeeds.put(key, newsFeed);
                savedNewsFeeds.add(newsFeed);
            }
        }

        User user = new User(appInstance.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", ""),
                appInstance.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", ""),
                appInstance.getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE).getString("gender", ""),
                appInstance.getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE).getString("age", ""),
                appInstance.getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE).getString("phone", ""),
                appInstance.getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE).getString("alternatePhone", ""),
                mapKeys, savedNewsFeeds);
        new APIClient().getUserAPI().register(user, new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                if (response.response.equals("success")) {
                    Toast.makeText(appInstance.getApplicationContext(), "User Info sent.", Toast.LENGTH_SHORT).show();
                    Log.d("SendDataPreviousDay : ", "User Info sent.");
                }
                else {
                    Toast.makeText(appInstance.getApplicationContext(), "User Info not sent.", Toast.LENGTH_SHORT).show();
                    Log.d("SendDataPreviousDay : ", "User Info not sent.");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(appInstance.getApplicationContext(), "User Info not sent.", Toast.LENGTH_SHORT).show();
                Log.d("SendDataPreviousDay : ", "User Info not sent (Retro error).");
            }
        });
    }


    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }
}
