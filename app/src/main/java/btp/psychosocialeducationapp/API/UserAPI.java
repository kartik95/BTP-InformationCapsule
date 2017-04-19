package btp.psychosocialeducationapp.API;

import btp.psychosocialeducationapp.API.UserInfo;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
/**
 * Created by gkartik on 19/4/17.
 */

public interface UserAPI {

    @POST("/api/app/login/patient")
    void login(@Body UserInfo user, Callback<LoginResponse> response);

    @POST("/api/app/register/patient")
    void register(@Body UserInfo userInfo, Callback<Response> response);

    @POST("/api/app/patient/applicationLogs")
    void sendCallLogs(@Body ApplicationLogsPost applicationLogsData, Callback<Response> response);
}
