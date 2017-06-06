package btp.psychosocialeducationapp.API;

import java.util.ArrayList;

import btp.psychosocialeducationapp.LogData;

/**
 * Created by gkartik on 19/4/17.
 */

public class ApplicationLogsPost {

    public UserInfo userInfo;
    public Log logData;

    public ApplicationLogsPost(UserInfo userInfo, Log logData) {
        this.userInfo = userInfo;
        this.logData = logData;
    }

    public UserInfo getUserInfo() {return this.userInfo;}
    public void setUserInfo(UserInfo userInfo) {this.userInfo = userInfo;}

    public Log getLogData() {return this.logData;}
    public void setLogData(Log logData) {this.logData = logData;}
}
