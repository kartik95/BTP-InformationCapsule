package btp.psychosocialeducationapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import btp.psychosocialeducationapp.API.APIClient;
import btp.psychosocialeducationapp.API.LoginResponse;
import btp.psychosocialeducationapp.API.User;
import btp.psychosocialeducationapp.API.UserAPI;
import btp.psychosocialeducationapp.API.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GPlusFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GPlusFragent";
    private int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
//    private Button signOutButton;
//    private Button disconnectButton;
//    private LinearLayout signOutView;
    private DBSingleton dbSingleton;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ImageView imgProfilePic;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private APIClient apiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        apiClient = new APIClient();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        dbSingleton = DBSingleton.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gplus, parent, false);

        signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
//        signOutButton = (Button) v.findViewById(R.id.sign_out_button);
        imgProfilePic = (ImageView) v.findViewById(R.id.img_profile_pic);

        mStatusTextView = (TextView) v.findViewById(R.id.status);
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.userdefault);
        imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });


//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
//                                updateUI(false);
//                            }
//                        });
//            }
//
//        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
////                Log.i(TAG, "This device is not supported.");
//                getActivity().finish();
//            }
//            return false;
//        }
//        return true;
//    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            if (result.getSignInAccount() != null) {

                if(getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE)
                        .getString("alreadySignedIn", "-1").equals("1")) {
                    updateUI(true, true);
                }
                else {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    String personName = acct.getDisplayName();
                    final String email = acct.getEmail();
                    Uri personPhotoUrl = acct.getPhotoUrl();
                    String personId = acct.getId();

                    SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit();
                    editor.putString("id", personId);
                    editor.putString("name", personName);
                    editor.putString("email", email);
                    editor.putString("url", String.valueOf(personPhotoUrl));
                    editor.commit();

                    UserInfo userInfo = new UserInfo(personId, email);
                    apiClient.getUserAPI().login(userInfo, new Callback<LoginResponse>() {
                        @Override
                        public void success(LoginResponse loginResponse, Response response) {
//                            Log.d("LoginAPIResponse : ", loginResponse.user.getEmailId());
                            if(loginResponse.response.equals("success")) {
                                User user = loginResponse.user;
                                Log.d("LoginAPIResponse : ", user.getSavedPreferences().toString());
                                Log.d("LoginAPIResponse : ", user.getEmailId());
                                Log.d("LoginAPIResponse : ", user.getSavedPreferencesPositions().toString());
//                                Log.d("LoginAPIResponse : ", user.getSavedPreferences().toString());
                                SharedPreferences.Editor ed = getActivity().getSharedPreferences("My_Prefs1", Context.MODE_PRIVATE)
                                        .edit();
                                ed.putString("gender", user.getGender());
                                ed.putString("age", user.getAge());
                                ed.putString("phone", user.getPrimaryContact());
                                ed.putString("alternatePhone", user.getAltContact());
                                ed.apply();

                                saveNewsFeedsToPreferences(user.getSavedPreferencesPositions(),
                                        user.getSavedPreferences());

                                updateUI(true, true);
                            }
                            else if(loginResponse.response.equals("failure")) {
                                getActivity().getSharedPreferences("RegisteredStatus", Context.MODE_PRIVATE)
                                        .edit().putString("regStatus", "NR").apply();
                                updateUI(true, false);
                            }
                            else {
                                Toast.makeText(getActivity(),"Login Error",Toast.LENGTH_SHORT).show();
                                getActivity().getSharedPreferences("RegisteredStatus", Context.MODE_PRIVATE)
                                        .edit().putString("regStatus", "NR").apply();
                                updateUI(false, false);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(),"Login API Error",Toast.LENGTH_SHORT).show();
                            Log.d("Retrofit Login Error : ", error.toString());
                            updateUI(false, false);
                        }
                    });

//                    updateUI(true, false);
                }
            }

        } else {
            // Signed out, show unauthenticated UI.
//            Toast.makeText(getActivity(),"Couldn't Sign In.",Toast.LENGTH_SHORT).show();
            updateUI(false, false);
        }
    }


    private void saveNewsFeedsToPreferences(List<String> savedPreferencesPositions,
                                            List<NewsFeed> savedPreferences){

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Prefs2", Context.MODE_PRIVATE).edit();
        editor.clear();

        Log.d("SIZE1 : ", Integer.toString(savedPreferencesPositions.size()));
        Log.d("SPP : ", savedPreferencesPositions.toString());
        Log.d("SIZE2 : ", Integer.toString(savedPreferences.size()));
//        Log.d("SP : ", savedPreferences.)

        Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new GPlusFragment.UriSerializer()).create();
//        List<String> mapKeys = new ArrayList<>(savedNewsFeeds.keySet());
        Set<String> keysSet = new HashSet<>(savedPreferencesPositions);
        Log.d("KEYSET : ", keysSet.toString());
        editor.putStringSet("mapKeys", keysSet);
        for(int i=0; i<savedPreferences.size(); i++){
            String value = gson.toJson(savedPreferences.get(i));
//            String value = gson.toJson(savedPreferences.get(Integer.parseInt(savedPreferencesPositions.get(i))));
            editor.putString(savedPreferencesPositions.get(i), value);
        }
        editor.apply();
    }


    private void updateUI(boolean signedIn, boolean alreadySignedIn) {
        if (signedIn && !alreadySignedIn) {

            String logString = "Signed in successfully.";
            if(dbSingleton.insertLog(new LogData(getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                    getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
                Toast.makeText(getContext(), "Log entered.", Toast.LENGTH_SHORT).show();
            }
            Log.d("Status : ", "Signed in successfully.");

//            List<LogData> receivedLogs = dbSingleton.getAllLogs();
//            for (int i=0; i<receivedLogs.size(); i++) {
//                Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                        receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//            }
            LogData receivedLog = dbSingleton.getLastLog();
            Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                    receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

            getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().putString("alreadySignedIn", "1").apply();
            getActivity().startActivity(new Intent(getActivity().getApplicationContext(), EditActivity.class));//.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            getActivity().finish();
        }
        else if(signedIn && alreadySignedIn){
            String logString = "Signed in successfully.";
            if(dbSingleton.insertLog(new LogData(getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("id", null),
                    getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString("email", null), logString))){
                Toast.makeText(getContext(), "Log entered.", Toast.LENGTH_SHORT).show();
            }
            Log.d("Status : ", "Signed in successfully.");

//            List<LogData> receivedLogs = dbSingleton.getAllLogs();
//            for (int i=0; i<receivedLogs.size(); i++) {
//                Log.d("Received Log : ", "{" + receivedLogs.get(i).getUserId() + ", " +
//                        receivedLogs.get(i).getEmail() + ", " + receivedLogs.get(i).getLog() + "}");
//            }
            LogData receivedLog = dbSingleton.getLastLog();
            Log.d("Received Last Log : ", "{" + receivedLog.getTimeStamp() + ", " + receivedLog.getUserId() + ", " +
                    receivedLog.getEmail() + ", " + receivedLog.getLog() + "}");

            getActivity().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().putString("alreadySignedIn", "1").apply();
            getActivity().startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));//.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            getActivity().finish();
        }
        else {
            mStatusTextView.setText(R.string.signed_out);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.userdefault);
            imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            signInButton.setVisibility(View.VISIBLE);
//            signOutButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }

    public class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}

