package btp.psychosocialeducationapp.API;

import java.util.ArrayList;
import java.util.List;

import btp.psychosocialeducationapp.API.UserInfo;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
/**
 * Created by gkartik on 19/4/17.
 */

public interface UserAPI {

    @POST("/api/app/capsule/login/user")
    void login(@Body UserInfo user, Callback<LoginResponse> response);

    @POST("/api/app/capsule/register/user")
    void register(@Body User user, Callback<Response> response);

    @POST("/api/app/capsule/user/log")
    void sendApplicationLogs(@Body List<ApplicationLogsPost> applicationLogsData, Callback<Response> response);
}
