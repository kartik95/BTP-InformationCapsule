package btp.psychosocialeducationapp.API;

import android.net.Uri;
import android.util.StringBuilderPrinter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkartik on 19/4/17.
 */

public class UserInfo {

    @SerializedName("userId")
    private String userId;

    @SerializedName("emailId")
    private String emailId;

//    @SerializedName("access_token")
//    private String access_token;
//
//    @SerializedName("appId_token")
//    private String appId_token;

//    @SerializedName("gcm_token")
//    private String gcm_token;

    public UserInfo(String userId, String emailId/*, String access_token, String appId_token*/) {
        this.userId = userId;
        this.emailId = emailId;
//        this.access_token = access_token;
//        this.appId_token = appId_token;
    }

    public String getUserId() {return this.userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getEmailId() {return this.emailId;}
    public void setEmailId(String emailId) {this.emailId = emailId;}

//    public String getAccess_token() {return this.access_token;}
//    public void setAccess_token(String access_token) {this.access_token = access_token;}
//
//    public String getAppId_token() {return this.appId_token;}
//    public void setAppId_token(String appId_token) {this.appId_token = appId_token;}

//    public String getGcm_token() {return this.gcm_token;}
//    public void setGcm_token(String gcm_token) {this.gcm_token = gcm_token;}
}
