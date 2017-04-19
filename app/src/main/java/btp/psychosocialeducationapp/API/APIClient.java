package btp.psychosocialeducationapp.API;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by gkartik on 19/4/17.
 */

public class APIClient {

    static final int CONNECT_TIMEOUT_MILLIS = 20 * 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 50 * 1000; // 20s
    static final Gson gson = new Gson();

    private final UserAPI userAPI;

    public APIClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RestAdapter adapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(Urls.BASE_GET_URL_ALT)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        userAPI = adapter.create(UserAPI.class);
    }

    public UserAPI getUserAPI() {
        return userAPI;
    }
}
