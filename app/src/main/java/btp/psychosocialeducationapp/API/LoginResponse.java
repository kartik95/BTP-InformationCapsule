package btp.psychosocialeducationapp.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkartik on 19/4/17.
 */

public class LoginResponse {

    @SerializedName("response")
    public String response;

    @SerializedName("user")
    public User user;
}
